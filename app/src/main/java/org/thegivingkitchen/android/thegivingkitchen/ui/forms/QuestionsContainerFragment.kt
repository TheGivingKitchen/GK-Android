package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_questions_container.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.prologue.FormPrologueFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.page.FormPageFragment
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener

class QuestionsContainerFragment: Fragment(), BackPressedListener {
    private lateinit var mPager: ViewPager
    private var questionPages = listOf<Page>()
    private lateinit var model: QuestionsContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(QuestionsContainerViewModel::class.java)

        if (arguments != null) {
            questionPages = arguments!!.getParcelableArrayList<Page>(FormPrologueFragment.questionPagesArg)!!.toList()
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
        mPager = viewPager_questionsContainer
        val questionPagesAdapter = ScreenSlidePagerAdapter(fragmentManager!!)
        mPager.adapter = questionPagesAdapter
        if (questionPagesAdapter.count < 2) {
            model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.SUBMIT)
        } else {
            model.setForwardButtonState(QuestionsContainerViewModel.Companion.ForwardButtonState.NEXT)
            mPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
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

    override fun onBackPressed(): Boolean {
        if (mPager.currentItem == 0) {
            return false
        }
        mPager.currentItem = mPager.currentItem - 1
        return true
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
        mPager.currentItem = mPager.currentItem - 1
    }

    private val nextButtonClickListener = View.OnClickListener {
        mPager.currentItem = mPager.currentItem + 1
    }

    private val submitButtonClickListener = View.OnClickListener {
        Toast.makeText(context, "submit pressed", Toast.LENGTH_SHORT).show()
    }

    private inner class ScreenSlidePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = questionPages.size

        override fun getItem(position: Int): Fragment {
            return FormPageFragment.newInstance(questionPages[position])
        }
    }
}