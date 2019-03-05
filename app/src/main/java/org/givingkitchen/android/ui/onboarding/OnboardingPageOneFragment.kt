package org.givingkitchen.android.ui.onboarding

import org.givingkitchen.android.R

class OnboardingPageOneFragment: BaseOnboardingPageFragment() {

    override fun headerText(): Int {
        return R.string.onboarding_page_one_header
    }

    override fun descriptionText(): Int {
        return R.string.onboarding_page_one_desciption
    }

    override fun imageSource(): Int {
        return R.drawable.pic_team_leader
    }
}