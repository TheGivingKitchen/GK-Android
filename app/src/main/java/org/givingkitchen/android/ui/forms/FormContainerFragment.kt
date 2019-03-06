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
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_questions_container.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.page.FormPageFragment
import org.givingkitchen.android.ui.forms.page.QuestionResponse
import org.givingkitchen.android.ui.forms.prologue.FormPrologueFragment
import org.givingkitchen.android.util.FragmentBackPressedListener
import org.givingkitchen.android.util.Services.moshi

class FormContainerFragment: Fragment(), FragmentBackPressedListener {
    private lateinit var questionPages: List<FormPageFragment>
    private lateinit var formId: String
    private lateinit var model: FormContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(FormContainerViewModel::class.java)

        if (arguments != null) {
            val form = arguments!!.getParcelable<Form>(FormPrologueFragment.formArg)
            questionPages = form!!.Pages!!.map { FormPageFragment.newInstance(it) }
            formId = form.ID!!
        }

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
            viewPager_questionsContainer.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) { }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

                override fun onPageSelected(position: Int) {
                    if (position == questionPagesAdapter.count-1) {
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
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(view.windowToken, 0) }
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
            // todo: store these questions and answers in Room instead of shared prefs
            val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with (sharedPref.edit()) {
                    putString(answer.question, answer.answer)
                    apply()
                }
            }
        }
        moveToNextQuestion()
    }

    private val submitButtonClickListener = View.OnClickListener {
        hideKeyboardIfShowing()
        var firstUnansweredPage: Int? = null

        for (i in 0 until questionPages.size) {
            val questionPage = questionPages[i]

            if (!questionPage.areAllQuestionsAnswered()) {
                questionPage.placeQuestionUnansweredWarnings()
                if (firstUnansweredPage == null) {
                    firstUnansweredPage = i
                }
            }
        }

        if (firstUnansweredPage != null) {
            viewPager_questionsContainer.setCurrentItem(firstUnansweredPage, true)
        } else {
            val submissionAnswers = arrayListOf<QuestionResponse>()

            for (i in 0 until questionPages.size) {
                submissionAnswers.addAll(questionPages[i].getQuestionResponses())
            }

            val submission = submissionAnswers.map { AnswerDictionaryEntry(it.id, it.answer) }
            val jsonAdapter = moshi.adapter(AnswerDictionary::class.java)
            val output = jsonAdapter.toJson(AnswerDictionary(submission))
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
