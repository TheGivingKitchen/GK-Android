package org.givingkitchen.android.ui.onboarding

import org.givingkitchen.android.R

class OnboardingPageTwoFragment: BaseOnboardingPageFragment() {

    override fun headerText(): Int {
        return R.string.onboarding_page_two_header
    }

    override fun descriptionText(): Int {
        return R.string.onboarding_page_two_desciption
    }

    override fun imageSource(): Int {
        return R.drawable.happy_waffle_house_employee
    }
}