package org.givingkitchen.android.ui.onboarding

import org.givingkitchen.android.R

class OnboardingPageThreeFragment: BaseOnboardingPageFragment() {

    override fun headerText(): Int {
        return R.string.onboarding_page_three_header
    }

    override fun descriptionText(): Int {
        return R.string.onboarding_page_three_description
    }

    override fun imageSource(): Int {
        return R.drawable.office_yoga
    }
}