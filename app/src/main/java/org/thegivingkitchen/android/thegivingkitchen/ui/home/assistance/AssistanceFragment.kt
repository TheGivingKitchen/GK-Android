package org.thegivingkitchen.android.thegivingkitchen.ui.home.assistance

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.Nullable
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import org.thegivingkitchen.android.thegivingkitchen.R
import android.widget.TextView
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_assistance.*

class AssistanceFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_assistance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crisis_grants_description_assistanceTab.setText(setHeaderText(), TextView.BufferType.SPANNABLE)
        for_you_button_assistanceTab.setOnClickListener(forYouButtonClickListener)
        for_someone_else_button_assistanceTab.setOnClickListener(forSomeoneElseButtonClickListener)
        learn_more_button_assistanceTab.setOnClickListener(learnMoreButtonClickListener)
    }

    private fun addColoredString(@StringRes str: Int, @ColorRes color: Int): SpannableString {
        val string = getString(str)
        val spannableString = SpannableString(getString(str))
        spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, color)), 0, string.length, 0)
        return spannableString
    }

    private fun setHeaderText(): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_title, R.color.gk_orange))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_description, R.color.gk_blue))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_illness, R.color.gk_orange))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_injury, R.color.gk_orange))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_death, R.color.gk_orange))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_comma, R.color.gk_blue))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_ampersand, R.color.gk_blue))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_disaster, R.color.gk_orange))
        builder.append(addColoredString(R.string.assistance_tab_crisis_grants_period, R.color.gk_blue))
        return builder
    }

    private val forYouButtonClickListener = Navigation.createNavigateOnClickListener(R.id.action_assistanceFragment_to_assistanceInquiryFormPrologueFragment)

    private val forSomeoneElseButtonClickListener = Navigation.createNavigateOnClickListener(R.id.action_assistanceFragment_to_assistanceInquiryReferralFormPrologueFragment)

    private val learnMoreButtonClickListener = View.OnClickListener {
        Toast.makeText(context, "learn more button clicked", Toast.LENGTH_SHORT).show()
    }
}