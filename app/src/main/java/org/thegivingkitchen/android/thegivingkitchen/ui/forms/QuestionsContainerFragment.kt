package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_questions_container.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.prologue.FormPrologueFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.page.FormPageFragment

class QuestionsContainerFragment: Fragment() {
    private lateinit var questionPages: List<FormPageFragment>
    private lateinit var model: QuestionsContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(QuestionsContainerViewModel::class.java)

        if (arguments != null) {
            val questions = arguments!!.getParcelableArrayList<Page>(FormPrologueFragment.questionPagesArg)!!.toList()
            questionPages = questions.map { FormPageFragment.newInstance(it) }
        }

        model.getForwardButtonState().observe(this, Observer<QuestionsContainerViewModel.Companion.ForwardButtonState> { forwardButtonState ->
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
            model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.SUBMIT)
        } else {
            model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.NEXT)
            viewPager_questionsContainer.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) { }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

                override fun onPageSelected(position: Int) {
                    if (position == questionPagesAdapter.count-1) {
                        model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.SUBMIT)
                    } else {
                        model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.NEXT)
                    }
                }
            })
        }

        // todo: use a constraintlayout group here
        backButtonText_questionsContainer.setOnClickListener(backButtonClickListener)
        backButtonIcon_questionsContainer.setOnClickListener(backButtonClickListener)
    }

    private fun updateForwardButton(state: QuestionsContainerViewModel.Companion.ForwardButtonState) {
        val forwardButtonClickAction: View.OnClickListener = when (state) {
            QuestionsContainerViewModel.Companion.ForwardButtonState.NEXT -> {
                nextButtonClickListener
            }
            QuestionsContainerViewModel.Companion.ForwardButtonState.SUBMIT -> {
                submitButtonClickListener
            }
        }
        nextButton_questionsContainer.setOnClickListener(forwardButtonClickAction)
        nextButton_questionsContainer.text = getString(state.text)
    }
    
    private val backButtonClickListener = View.OnClickListener {
        // viewPager_questionsContainer.setCurrentItem(viewPager_questionsContainer.currentItem - 1, true)

        val questionPage = questionPages[viewPager_questionsContainer.currentItem]

        if (!questionPage.areAllQuestionsAnswered()) {
            questionPage.placeQuestionUnansweredWarnings()
        }
    }

    private val nextButtonClickListener = View.OnClickListener {
        val currentItem = viewPager_questionsContainer.currentItem
        for (answer in questionPages[currentItem].getQuestionsAndAnswers()) {
            val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            if (sharedPref != null) {
                with (sharedPref.edit()) {
                    putString(answer.first, answer.second)
                    apply()
                }
            }
        }

        viewPager_questionsContainer.setCurrentItem(currentItem + 1, true)
    }

    private val submitButtonClickListener = View.OnClickListener {
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
            // todo: submit the form
        }
    }

    private inner class ScreenSlidePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = questionPages.size

        override fun getItem(position: Int): Fragment {
            return questionPages[position]
        }
    }
}