package org.thegivingkitchen.android.thegivingkitchen.ui.forms.assistanceinquiryform

import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.BaseFormsPrologueFragment

class AssistanceInquiryFormPrologueFragment: BaseFormsPrologueFragment() {
    override fun titleText(): Int {
        return R.string.assistance_inquiry_form_title
    }

    override fun subtitleText(): Int {
        return  R.string.assistance_inquiry_form_subtitle
    }

    override fun descriptionText(): Int {
        return  R.string.assistance_inquiry_form_description
    }
}