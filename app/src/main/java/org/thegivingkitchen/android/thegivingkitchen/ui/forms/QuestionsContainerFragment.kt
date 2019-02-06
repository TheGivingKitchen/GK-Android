package org.thegivingkitchen.android.thegivingkitchen.ui.forms

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
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener

class QuestionsContainerFragment: Fragment(), BackPressedListener {
    private lateinit var mPager: ViewPager
    private var questionPages = listOf<Page>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            questionPages = arguments!!.getParcelableArrayList<Page>(FormPrologueFragment.questionPagesArg)!!.toList()
        }
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
            transformNextButtonToSubmit()
        } else {
            mPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) { }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }
                override fun onPageSelected(position: Int) {
                    if (position == questionPagesAdapter.count-1) {
                        transformNextButtonToSubmit()
                    } else {
                        nextButton_questionsContainer.text = getString(R.string.forms_questions_next)
                    }
                }
            })
        }

        // todo: use a constraintlayout group here
        backButtonText_questionsContainer.setOnClickListener(backButtonClickListener)
        backButtonIcon_questionsContainer.setOnClickListener(backButtonClickListener)
        nextButton_questionsContainer.setOnClickListener(nextButtonClickListener)
    }

    override fun onBackPressed(): Boolean {
        if (mPager.currentItem == 0) {
            return false
        }
        mPager.currentItem = mPager.currentItem - 1
        return true
    }
    
    private fun transformNextButtonToSubmit() {
        nextButton_questionsContainer.text = getString(R.string.forms_questions_submit)
    }

    private val backButtonClickListener = View.OnClickListener {
        mPager.currentItem = mPager.currentItem - 1
    }

    private val nextButtonClickListener = View.OnClickListener {
        mPager.currentItem = mPager.currentItem + 1
    }

    private inner class ScreenSlidePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = questionPages.size

        override fun getItem(position: Int): Fragment {
            return FormPageFragment.newInstance(questionPages[position])
        }
    }
}