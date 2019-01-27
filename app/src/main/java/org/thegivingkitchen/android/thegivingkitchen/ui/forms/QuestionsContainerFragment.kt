package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_questions_container.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.prologue.FormPrologueFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.question.QuestionFragment
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
        val pagerAdapter = ScreenSlidePagerAdapter(fragmentManager!!)
        mPager.adapter = pagerAdapter

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

    private val backButtonClickListener = View.OnClickListener {
        mPager.currentItem = mPager.currentItem - 1
    }

    private val nextButtonClickListener = View.OnClickListener {
        mPager.currentItem = mPager.currentItem + 1
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = questionPages.size

        override fun getItem(position: Int): Fragment {
            return QuestionFragment.newInstance(questionPages[position])
        }
    }
}