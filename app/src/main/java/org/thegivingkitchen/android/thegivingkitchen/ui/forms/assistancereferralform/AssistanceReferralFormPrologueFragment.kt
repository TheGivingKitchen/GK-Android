package org.thegivingkitchen.android.thegivingkitchen.ui.forms.assistancereferralform

import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.BaseFormsPrologueFragment

class AssistanceReferralFormPrologueFragment: BaseFormsPrologueFragment() {
    override fun titleText(): Int {
        return R.string.assistance_referral_form_title
    }

    override fun subtitleText(): Int {
        return  R.string.assistance_referral_form_subtitle
    }

    override fun descriptionText(): Int {
        return  R.string.assistance_referral_form_description
    }
}