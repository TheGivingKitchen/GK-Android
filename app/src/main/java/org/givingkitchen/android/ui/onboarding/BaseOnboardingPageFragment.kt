package org.givingkitchen.android.ui.onboarding

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_onboarding_page.*
import org.givingkitchen.android.R

abstract class BaseOnboardingPageFragment: Fragment() {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboarding_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header_onboardingPage.text = getText(headerText())
        description_onboardingPage.text = getText(descriptionText())
        image_onboardingPage.setImageDrawable(resources.getDrawable(imageSource(), context!!.theme))
    }

    @StringRes abstract fun headerText(): Int

    @StringRes abstract fun descriptionText(): Int

    @DrawableRes abstract fun imageSource(): Int
}