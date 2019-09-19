package org.givingkitchen.android.ui.homescreen.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_about.*
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.analytics.Events
import org.givingkitchen.android.util.Constants.givingKitchenUrl
import org.givingkitchen.android.util.CustomTabs

class AboutFragment : Fragment()  {

    companion object {
        const val newsletterSignupUrl = "https://thegivingkitchen.us3.list-manage.com/subscribe?u=8ce234d2bdddfb2c1ba574d4f&id=9071a9bab9"
        const val howItWorksPdfUrl = "https://firebasestorage.googleapis.com/v0/b/thegivingkitchen-cdd28.appspot.com/o/howitworks.pdf?alt=media&token=7bcc47e7-45f5-4b71-b4ad-9071e9d8efd8"
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

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val facebookSectionExpanded = sharedPref?.getBoolean(getString(R.string.facebook_groups_expanded_key), true) ?: true
        setFacebookGroupsExpandedState(facebookSectionExpanded)
        collapseFacebookButton_aboutTab.setOnClickListener(collapseFacebookSectionClickListener)
        joinUsButton_aboutTab.setOnClickListener(joinFacebookGroupsClickListener)
    }

    private val aboutUsButtonClickListener = View.OnClickListener {
        Analytics.logLearnedMore("crisis_grant_info_graphic")
        Navigation.findNavController(view!!).navigate(R.id.howItWorksFragment)
    }

    private val newsletterSignupClickListener = View.OnClickListener {
        Analytics.logEvent(Events.NEWSLETTER_SIGNUP_STARTED)

        CustomTabs.openCustomTab(context, newsletterSignupUrl)
    }

    private val storyOneClickListener = View.OnClickListener {
        Analytics.logLearnedMore("story_the_performer")
        CustomTabs.openCustomTab(context, storyOneUrl)
    }

    private val storyTwoClickListener = View.OnClickListener {
        Analytics.logLearnedMore("when_irma_hit")
        CustomTabs.openCustomTab(context, storyTwoUrl)
    }

    private val storyThreeClickListener = View.OnClickListener {
        Analytics.logLearnedMore("comfort_in_athens")
        CustomTabs.openCustomTab(context, storyThreeUrl)
    }

    private val feedbackPositiveClickListener = View.OnClickListener {
        Analytics.logEvent(Events.FEEDBACK_POSITIVE)
        val rateAppDialog = RateAppDialogFragment()
        rateAppDialog.setOnCompleteListener {
            if (!it) {
                goToPlayStore()
            }
        }
        rateAppDialog.show(fragmentManager, "Rate_App_Dialog")
    }

    private val feedbackNeutralClickListener = View.OnClickListener {
        Analytics.logEvent(Events.FEEDBACK_COMMENT)

        Navigation.findNavController(view!!).navigate(R.id.feedbackFragment)
    }

    private val feedbackNegativeClickListener = View.OnClickListener {
        Analytics.logEvent(Events.FEEDBACK_REPORT_PROBLEM)

        val emailIntent: Intent = Intent().apply {
            action = Intent.ACTION_SENDTO
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.about_tab_gk_email_address))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.about_tab_gk_email_subject))
            type = "text/plain"
        }

        if (emailIntent.resolveActivity(context!!.packageManager) != null) {
            startActivity(emailIntent)
        } else {
            CustomTabs.openCustomTab(context, gkContactUrl)
        }
    }

    private val collapseFacebookSectionClickListener = View.OnClickListener {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val facebookSectionExpanded = sharedPref?.let {
            val currentValue = it.getBoolean(getString(R.string.facebook_groups_expanded_key), true)
            !currentValue
        } ?: run {
            true
        }
        setFacebookGroupsExpandedState(facebookSectionExpanded)
    }

    private val joinFacebookGroupsClickListener = View.OnClickListener {
        Navigation.findNavController(view!!).navigate(R.id.facebookGroupsFragment)
    }

    private fun setFacebookGroupsExpandedState(expanded: Boolean): Boolean {
        activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)?.let {
            with(it.edit()) {
                putBoolean(getString(R.string.facebook_groups_expanded_key), expanded)
                apply()
            }
        }

        return if (expanded) {
            collapseFacebookButton_aboutTab.setImageDrawable(collapseFacebookButton_aboutTab.resources.getDrawable(R.drawable.ic_expand_less, collapseFacebookButton_aboutTab.context.theme))
            facebookGroupsDescription_aboutTab.visibility = View.VISIBLE
            facebookBottomDivider_aboutTab.visibility = View.VISIBLE
            joinUsButton_aboutTab.visibility = View.VISIBLE
            true
        } else {
            collapseFacebookButton_aboutTab.setImageDrawable(collapseFacebookButton_aboutTab.resources.getDrawable(R.drawable.ic_expand_more, collapseFacebookButton_aboutTab.context.theme))
            facebookGroupsDescription_aboutTab.visibility = View.GONE
            facebookBottomDivider_aboutTab.visibility = View.GONE
            joinUsButton_aboutTab.visibility = View.GONE
            false
        }
    }

    private fun goToPlayStore() {
        val appPackageName = context!!.packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (exception: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}