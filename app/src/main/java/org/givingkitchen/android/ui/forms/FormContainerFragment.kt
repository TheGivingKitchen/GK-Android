package org.givingkitchen.android.ui.forms

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
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
import org.givingkitchen.android.ui.forms.page.QuestionResponse
import org.givingkitchen.android.ui.forms.prologue.FormPrologueFragment
import org.givingkitchen.android.util.*
import org.givingkitchen.android.util.Services.moshi
import okhttp3.*
import okhttp3.Request
import okhttp3.Response
import org.givingkitchen.android.R
import java.io.IOException


class FormContainerFragment : Fragment(), FragmentBackPressedListener {
    private lateinit var questionPages: List<FormPageFragment>
    private lateinit var formId: String
    private lateinit var model: FormContainerViewModel
    private lateinit var jsonAdapter: JsonAdapter<WufooResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(FormContainerViewModel::class.java)

        if (arguments != null) {
            val form = arguments!!.getParcelable<Form>(FormPrologueFragment.formArg)
            questionPages = form!!.Pages!!.map { FormPageFragment.newInstance(it) }
            formId = form.ID!!
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
        val questionPagesAdapter = ScreenSlidePagerAdapter(fragmentManager!!)
        viewPager_questionsContainer.adapter = questionPagesAdapter
        if (questionPagesAdapter.count < 2) {
            model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.SUBMIT)
        } else {
            model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.NEXT)
            viewPager_questionsContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    if (position == questionPagesAdapter.count - 1) {
                        model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.SUBMIT)
                    } else {
                        model.setForwardButtonState(FormContainerViewModel.Companion.ForwardButtonState.NEXT)
                    }
                }
            })
        }

        // todo: use a textview drawable here
        backButtonText_questionsContainer.setOnClickListener(backButtonClickListener)
        backButtonIcon_questionsContainer.setOnClickListener(backButtonClickListener)
    }

    override fun onBackPressed(): Boolean {
        return if (showingFirstQuestion()) {
            // Already on the first page, fall back to default back press handling
            false
        } else {
            moveToPreviousQuestion()
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

    private fun showingFirstQuestion(): Boolean {
        return viewPager_questionsContainer.currentItem == 0
    }

    private fun moveToNextQuestion() {
        val currentItem = viewPager_questionsContainer.currentItem
        viewPager_questionsContainer.setCurrentItem(currentItem + 1, true)
    }

    private fun moveToPreviousQuestion() {
        viewPager_questionsContainer.setCurrentItem(viewPager_questionsContainer.currentItem - 1, true)
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
        val currentItem = viewPager_questionsContainer.currentItem
        for (answer in questionPages[currentItem].getQuestionResponses()) {
            val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with(sharedPref.edit()) {
                    putString(answer.question, answer.answer)
                    apply()
                }
            }
        }
        moveToNextQuestion()
    }

    private val submitButtonClickListener = View.OnClickListener {
        hideKeyboardIfShowing()

        val submissionAnswers = arrayListOf<QuestionResponse>()

        for (i in 0 until questionPages.size) {
            submissionAnswers.addAll(questionPages[i].getQuestionResponses())
        }

        val submission = submissionAnswers.map { AnswerDictionaryEntry(it.id, it.answer) }
        val jsonAdapter = moshi.adapter(AnswerDictionary::class.java)
        val output = jsonAdapter.toJson(AnswerDictionary(submission))

        val str = post()


    }

    private fun post() {
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

        val requestbody = FormBody.Builder()
        val submissionAnswers = arrayListOf<QuestionResponse>()

        for (i in 0 until questionPages.size) {
            submissionAnswers.addAll(questionPages[i].getQuestionResponses())
        }

        for (submissionAnswer in submissionAnswers) {
            requestbody.add(submissionAnswer.id, submissionAnswer.answer)
        }

        val request = Request.Builder()
                .url(getString(R.string.form_done_submit_url, formId))
                .post(requestbody.build())
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handleFormSubmissionError("Error submitting form with id $formId: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.code() == Constants.httpUnauthorizedError) {
                    handleFormSubmissionError("Invalid authorization credentials for form $formId")
                    return
                }

                val wufooResponse = jsonAdapter.nullSafe().fromJson(response.body()!!.string())

                if (wufooResponse!!.Success == 1) {
                    goToDonePage()
                } else {
                    goToErrorPage(wufooResponse)
                }
            }
        })
    }

    private fun goToDonePage() {
        val donePage: DonePage = try {
            arguments!!.getEnum<DonePage>(Constants.donePageArg)
        } catch (e: KotlinNullPointerException) {
            DonePage.DEFAULT
        }
        val args = Bundle()
        args.putEnum(Constants.donePageArg, donePage)
        Navigation.findNavController(view!!).navigate(R.id.formDoneFragment, args)
    }

    private fun goToErrorPage(wufooResponse: WufooResponse) {
        if (wufooResponse.FieldErrors == null) {
            handleFormSubmissionError("Wufoo returned Success = 0, but FieldErrors were null")
            return
        }

        val erroredQuestions = HashMap<String, String>()

        for (fieldError in wufooResponse.FieldErrors) {
            erroredQuestions.put(fieldError.ID, fieldError.ErrorText)
        }

        for (i in 0 until questionPages.size) {
            val questionPage = questionPages[i]

            for (question in questionPage.)
        }

        viewPager_questionsContainer.setCurrentItem(firstUnansweredPage, true)
    }

    private fun handleFormSubmissionError(logMessage: String, @StringRes toastMessage: Int = R.string.form_done_submit_error) {
        Crashlytics.log(logMessage)
        activity!!.runOnUiThread {
            Toast.makeText(context, getString(toastMessage), Toast.LENGTH_SHORT).show()
        }
    }

    private inner class ScreenSlidePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = questionPages.size

        override fun getItem(position: Int): Fragment {
            return questionPages[position]
        }
    }
}

class AnswerDictionaryEntry(val id: String, val answer: String)
class AnswerDictionary(val entries: List<AnswerDictionaryEntry>)




