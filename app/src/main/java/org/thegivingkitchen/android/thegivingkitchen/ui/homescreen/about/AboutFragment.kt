package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.about

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_about.*
import org.thegivingkitchen.android.thegivingkitchen.R

class AboutFragment : Fragment()  {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutUsButton_aboutTab.setOnClickListener(aboutUsButtonClickListener)
        newsletterSignup_aboutTab.setOnClickListener(newsletterSignupClickListener)
        storyOneImage_aboutTab.setOnClickListener(storyOneClickListener)
        storyOneLink_aboutTab.setOnClickListener(storyOneClickListener)
        storyTwoImage_aboutTab.setOnClickListener(storyTwoClickListener)
        storyTwoLink_aboutTab.setOnClickListener(storyTwoClickListener)
        storyThreeImage_aboutTab.setOnClickListener(storyThreeClickListener)
        storyThreeLink_aboutTab.setOnClickListener(storyThreeClickListener)
        feedbackPositive_aboutTab.setOnClickListener(feedbackPositiveClickListener)
        feedbackNeutral_aboutTab.setOnClickListener(feedbackNeutralClickListener)
        feedbackNegative_aboutTab.setOnClickListener(feedbackNegativeClickListener)
    }

    private val aboutUsButtonClickListener = View.OnClickListener {
        Toast.makeText(context, "How it works button clicked", Toast.LENGTH_SHORT).show()
    }

    private val newsletterSignupClickListener = View.OnClickListener {
        Toast.makeText(context, "Newsletter signup clicked", Toast.LENGTH_SHORT).show()
    }

    private val storyOneClickListener = View.OnClickListener {
        Toast.makeText(context, "Story One clicked", Toast.LENGTH_SHORT).show()
    }

    private val storyTwoClickListener = View.OnClickListener {
        Toast.makeText(context, "Story Two clicked", Toast.LENGTH_SHORT).show()
    }

    private val storyThreeClickListener = View.OnClickListener {
        Toast.makeText(context, "Story Three clicked", Toast.LENGTH_SHORT).show()
    }

    private val feedbackPositiveClickListener = View.OnClickListener {
        Toast.makeText(context, "+ Feedback clicked", Toast.LENGTH_SHORT).show()
    }

    private val feedbackNeutralClickListener = View.OnClickListener {
        Toast.makeText(context, "/ Feedback clicked", Toast.LENGTH_SHORT).show()
    }

    private val feedbackNegativeClickListener = View.OnClickListener {
        Toast.makeText(context, "- Feedback clicked", Toast.LENGTH_SHORT).show()
    }
}