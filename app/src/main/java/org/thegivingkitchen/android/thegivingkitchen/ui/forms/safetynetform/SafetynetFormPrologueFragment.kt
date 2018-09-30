package org.thegivingkitchen.android.thegivingkitchen.ui.forms.safetynetform

import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.BaseFormsPrologueFragment

class SafetynetFormPrologueFragment: BaseFormsPrologueFragment() {
    override fun titleText(): Int {
        return R.string.safetynet_form_title
    }

    override fun subtitleText(): Int {
        return  R.string.safetynet_form_subtitle
    }

    override fun descriptionText(): Int {
        return  R.string.safetynet_form_description
    }
}