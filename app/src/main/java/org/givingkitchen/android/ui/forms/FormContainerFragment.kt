package org.givingkitchen.android.ui.forms

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_questions_container.*
import org.givingkitchen.android.ui.forms.page.FormPageFragment
import org.givingkitchen.android.ui.forms.prologue.FormPrologueFragment
import org.givingkitchen.android.util.*
import okhttp3.*
import okhttp3.Request
import okhttp3.Response
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.analytics.Events
import org.givingkitchen.android.analytics.Parameter
import org.givingkitchen.android.ui.forms.page.QuestionType
import java.io.IOException

class FormContainerFragment : Fragment(), FragmentBackPressedListener {
    private lateinit var form: Form
    private lateinit var model: FormContainerViewModel
    private lateinit var jsonAdapter: JsonAdapter<WufooResponse>
    private lateinit var formPagerAdapter: FormPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(FormContainerViewModel::class.java)

        if (arguments != null) {
            form = arguments!!.getParcelable(FormPrologueFragment.formArg)!!

            val params = mapOf(Parameter.FORM_NAME to form.FormTitle, Parameter.FORM_ID to form.ID)
            Analytics.logEvent(Events.FORM_VIEW_DETAILS, params)
        }

        jsonAdapter = Services.moshi.adapter(WufooResponse::class.java)
        model.getForwardButtonState().observe(this, Observer<FormContainerViewModel.Companion.ForwardButtonState> { forwardButtonState ->
            updateForwardButton(forwardButtonState!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.getGivingKitchenSharedPreferences()?.let {
            for (currentFormPage in form.Pages) {
                currentFormPage.questions?.let { questions ->
                    for (question in questions) {
                        placeSavedAnswersInForm(question, it)
                    }
                }
            }
        }

        formPagerAdapter = FormPagerAdapter(fragmentManager!!)
        viewPager_questionsContainer.adapter = formPagerAdapter
        if (formPagerAdapter.count < 2) {
            model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.SUBMIT)
        } else {
            model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.NEXT)
            viewPager_questionsContainer.addOnPageChangeListener(formPageChangeListener)
        }

        // todo: use a textview drawable here
        backButtonText_questionsContainer.setOnClickListener(backButtonClickListener)
        backButtonIcon_questionsContainer.setOnClickListener(backButtonClickListener)
    }

    override fun onBackPressed(): Boolean {
        return if (viewPager_questionsContainer.currentItem == 0) {
            false
        } else {
            viewPager_questionsContainer.setCurrentItem(viewPager_questionsContainer.currentItem - 1, true)
            true
        }
    }

    private fun updateForwardButton(state: FormContainerViewModel.Companion.ForwardButtonState) {
        val forwardButtonClickAction: View.OnClickListener = when (state) {
            FormContainerViewModel.Companion.ForwardButtonState.NEXT -> {
                nextButtonClickListener
            }
            FormContainerViewModel.Companion.ForwardButtonState.SUBMIT -> {
                submitButtonClickListener
            }
        }
        nextButton_questionsContainer.setOnClickListener(forwardButtonClickAction)
        nextButton_questionsContainer.text = getString(state.text)
    }

    private fun hideKeyboardIfShowing() {
        val view = activity?.currentFocus
        view?.let {
            val inputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private val backButtonClickListener = View.OnClickListener {
        if (!onBackPressed()) {
            findNavController().navigateUp()
        }
    }

    private val nextButtonClickListener = View.OnClickListener {
        hideKeyboardIfShowing()
        savePageAnswers()

        viewPager_questionsContainer.setCurrentItem(viewPager_questionsContainer.currentItem + 1, true)
    }

    private val formPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            if (position == formPagerAdapter.count - 1) {
                model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.SUBMIT)
            } else {
                model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.NEXT)
            }
        }
    }

    private val submitButtonClickListener = View.OnClickListener {
        hideKeyboardIfShowing()
        savePageAnswers()
        nextButton_questionsContainer.visibility = View.GONE
        progressBar_questionsContainer.visibility = View.VISIBLE

        val requestBody = FormBody.Builder()

        form.DefaultAnswers?.let {
            for (defaultAnswer in it) {
                requestBody.add(defaultAnswer.ID, defaultAnswer.Answer)
            }
        }

        for (currentFormPage in form.Pages) {
            currentFormPage.questions?.let { questions ->
                for (question in questions) {
                    question.answers?.let { answers ->
                        for ((fieldId, answer) in answers) {
                            if (answer.isNotBlank()) {
                                requestBody.add(fieldId, answer)
                            }
                        }
                    }
                }
            }
        }

        submitForm(Request.Builder()
                .url(getString(R.string.form_done_submit_url, form.ID))
                .post(requestBody.build())
                .build())
    }

    private fun submitForm(request: Request) {
        val client = OkHttpClient.Builder()
                .authenticator(object : Authenticator {
                    override fun authenticate(route: Route?, response: Response): Request? {
                        if (response.request().header("Authorization") != null) {
                            return null // Give up, we've already attempted to authenticate.
                        }

                        val credentials = Credentials.basic("M7Q4-OZTA-MBXK-S2WT", "welcometotgk12345")
                        return response.request().newBuilder()
                                .header("Authorization", credentials)
                                .build()
                    }
                }).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handleFormSubmissionError("Error submitting form with id ${form.ID}: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code() == Constants.httpUnauthorizedError) {
                    handleFormSubmissionError("Invalid authorization credentials for form ${form.ID}")
                    return
                }

                val wufooResponse = jsonAdapter.nullSafe().fromJson(response.body()!!.string())

                activity!!.runOnUiThread {
                    nextButton_questionsContainer.visibility = View.VISIBLE
                    progressBar_questionsContainer.visibility = View.GONE

                    if (wufooResponse!!.Success == 1) {
                        goToDonePage()
                    } else {
                        goToErrorPage(wufooResponse)
                    }
                }
            }
        })
    }

    private fun savePageAnswers() {
        val currentPosition = viewPager_questionsContainer.currentItem
        val currentFragment = formPagerAdapter.getRegisteredFragment(currentPosition)

        for (questionView in currentFragment.questionViews) {
            questionView.saveAnswer(form.ID, activity.getGivingKitchenSharedPreferences())
        }
    }

    private fun placeSavedAnswersInForm(question: Question, sharedPref: SharedPreferences) {
        if (question.answers == null) {
            question.answers = HashMap()
        }

        question.Type?.let { type ->
            when (type) {
                QuestionType.shortname -> {
                    placeMultipleSavedAnswersInForm(question, 2, sharedPref)
                }
                QuestionType.address -> {
                    placeMultipleSavedAnswersInForm(question, 5, sharedPref)
                }
                else -> {
                    val savedAnswer = sharedPref.getString(form.ID + question.ID, null)
                    if (savedAnswer != null) {
                        question.answers!![question.ID] = savedAnswer
                    }
                }
            }
        }
    }

    private fun placeMultipleSavedAnswersInForm(question: Question, formFieldsCount: Int, sharedPref: SharedPreferences) {
        for (count in 0 until formFieldsCount) {
            val startingFieldIdNumber = question.ID.split("Field")[1].toInt()
            val fieldId = context!!.getString(R.string.forms_questions_field_id_format, startingFieldIdNumber+count)

            val savedAnswer = sharedPref.getString(form.ID + fieldId, null)

            if (savedAnswer != null) {
                question.answers!![fieldId] = savedAnswer
            }
        }
    }

    private fun goToDonePage() {
        val donePage: DonePage = arguments!!.getEnum<DonePage>(Constants.donePageArg, DonePage.DEFAULT)
        val args = Bundle()
        args.putEnum(Constants.donePageArg, donePage)
        Navigation.findNavController(view!!).navigate(R.id.formDoneFragment, args)
    }

    private fun goToErrorPage(wufooResponse: WufooResponse) {
        if (wufooResponse.FieldErrors == null) {
            handleFormSubmissionError("Wufoo returned Success = 0, but FieldErrors were null")
            return
        }

        val fieldErrorIds = hashMapOf<String, String>()
        fieldErrorIds.putAll(wufooResponse.FieldErrors.map { Pair(it.ID, it.ErrorText) })

        /* add all warning messages to Form object */
        var firstErroredPage: Int? = null

        for (i in 0 until form.Pages.size) {
            val page = form.Pages[i]

            page.questions?.let { questions ->
                for (question in questions) {
                    if (question.ID in fieldErrorIds) {
                        question.warning = fieldErrorIds[question.ID]
                        if (firstErroredPage == null) {
                            firstErroredPage = i
                        }
                    }
                }
            }
        }

        /* navigate to the first page that has an error */
        if (firstErroredPage != null) {
            viewPager_questionsContainer.setCurrentItem(firstErroredPage!!, true)
        } else {
            handleFormSubmissionError("Wufoo returned Success = 0, but could not find a page with errors")
        }
    }

    private fun handleFormSubmissionError(logMessage: String, @StringRes toastMessage: Int = R.string.form_done_submit_error) {
        Crashlytics.log(logMessage)
        activity!!.runOnUiThread {
            Toast.makeText(context, getString(toastMessage), Toast.LENGTH_SHORT).show()
        }
    }

    private inner class FormPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        val registeredFragments = SparseArray<Fragment>()

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val fragment = super.instantiateItem(container, position) as Fragment
            registeredFragments.put(position, fragment)
            return super.instantiateItem(container, position)
        }

        override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
            registeredFragments.remove(position)
            super.destroyItem(container, position, item)
        }

        fun getRegisteredFragment(position: Int): FormPageFragment {
            return registeredFragments.get(position) as FormPageFragment
        }

        override fun getCount(): Int = form.Pages.size

        override fun getItem(position: Int): Fragment {
            return FormPageFragment.newInstance(form.Pages[position], form.ID)
        }
    }
}
