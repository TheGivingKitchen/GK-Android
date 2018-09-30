package org.thegivingkitchen.android.thegivingkitchen.ui.forms.volunteerform

import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.BaseFormsPrologueFragment

class VolunteerFormPrologueFragment: BaseFormsPrologueFragment() {
    override fun titleText(): Int {
        return R.string.volunteer_form_title
    }

    override fun subtitleText(): Int {
        return  R.string.volunteer_form_subtitle
    }

    override fun descriptionText(): Int {
        return  R.string.volunteer_form_description
    }
}