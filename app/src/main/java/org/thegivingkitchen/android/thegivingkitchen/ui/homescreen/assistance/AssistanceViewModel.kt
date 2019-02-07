package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.firebaseStorageUrl

class AssistanceViewModel: ViewModel() {
    companion object {
        const val learnMoreURL = "https://connect.clickandpledge.com/w/Form/d11bff52-0cd0-44d8-9403-465614e4f342"
        const val selfAssistanceInquiryUrl = "$firebaseStorageUrl/forms/assistanceInquirySelf.json"
        const val referralAssistanceInquiryUrl = "$firebaseStorageUrl/forms/assistanceInquiryReferral.json"
    }

    private fun addColoredString(context: Context, @StringRes str: Int, @ColorRes color: Int): SpannableString {
        val string = context.getString(str)
        val spannableString = SpannableString(string)
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), 0, string.length, 0)
        return spannableString
    }

    fun setHeaderText(context: Context): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_title, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_description, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_illness, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_injury, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_death, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_ampersand, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_disaster, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_period, R.color.gk_blue))
        return builder
    }
}