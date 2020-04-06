package org.givingkitchen.android.ui.homescreen.assistance

import androidx.lifecycle.ViewModel
import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import org.givingkitchen.android.R
import org.givingkitchen.android.util.Constants.firebaseStorageUrl
import org.givingkitchen.android.util.Constants.givingKitchenUrl

class AssistanceViewModel: ViewModel() {
    companion object {
        const val assistanceLearnMoreURL = "$givingKitchenUrl/what-we-do"
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
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_description, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_illness, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_injury, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_death, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(context,  R.string.assistance_tab_crisis_grants_or, R.color.gk_blue))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_disaster, R.color.gk_orange))
        builder.append(addColoredString(context, R.string.assistance_tab_crisis_grants_period, R.color.gk_blue))
        return builder
    }
}