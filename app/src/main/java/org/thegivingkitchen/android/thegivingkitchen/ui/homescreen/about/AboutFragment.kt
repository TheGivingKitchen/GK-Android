package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.about

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_about.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs

class AboutFragment : Fragment()  {

    companion object {
        const val newsletterSignupUrl = "https://thegivingkitchen.us3.list-manage.com/subscribe?u=8ce234d2bdddfb2c1ba574d4f&id=9071a9bab9"
        const val gkContactUrl = "$givingKitchenUrl/contact"
        const val storyOneUrl = "$givingKitchenUrl/reggie-ealy"
        const val storyTwoUrl = "$givingKitchenUrl/when-irma-hit"
        const val storyThreeUrl = "$givingKitchenUrl/shannon-brown-shares-her-story-in-athens"
    }

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
        CustomTabs.openCustomTab(context, newsletterSignupUrl)
    }

    private val storyOneClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, storyOneUrl)
    }

    private val storyTwoClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, storyTwoUrl)
    }

    private val storyThreeClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, storyThreeUrl)
    }

    private val feedbackPositiveClickListener = View.OnClickListener {
        Toast.makeText(context, "+ Feedback clicked", Toast.LENGTH_SHORT).show()
    }

    private val feedbackNeutralClickListener = View.OnClickListener {
        Toast.makeText(context, "/ Feedback clicked", Toast.LENGTH_SHORT).show()
    }

    private val feedbackNegativeClickListener = View.OnClickListener {
        val emailIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.about_tab_gk_email_address))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_tab_gk_email_subject))
            type = "text/plain"
        }

        // todo: see if the email app opens on a real device
        if (emailIntent.resolveActivity(context!!.packageManager) != null) {
            startActivity(emailIntent)
        } else {
            CustomTabs.openCustomTab(context, gkContactUrl)
        }
    }
}