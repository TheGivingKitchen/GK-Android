package org.givingkitchen.android.ui.onboarding

import org.givingkitchen.android.R

class OnboardingPageFourFragment: BaseOnboardingPageFragment() {

    override fun headerText(): Int {
        return R.string.onboarding_page_four_header
    }

    override fun descriptionText(): Int {
        return R.string.onboarding_page_four_description
    }

    override fun imageSource(): Int {
        return R.drawable.team_hidi_patches
    }
}