package org.givingkitchen.android.ui.onboarding

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_onboarding_container.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.FragmentBackPressedListener

class OnboardingContainerFragment: Fragment(), FragmentBackPressedListener {
    private val onboardingPages = listOf(OnboardingPageOneFragment(), OnboardingPageTwoFragment(), OnboardingPageThreeFragment(), OnboardingPageFourFragment())
    private lateinit var model: OnboardingContainerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(OnboardingContainerViewModel::class.java)
        model.getForwardButtonState().observe(this, Observer<OnboardingContainerViewModel.Companion.ForwardButtonState> { forwardButtonState ->
            updateForwardButton(forwardButtonState!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_onboarding_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onboardingPagesAdapter = ScreenSlidePagerAdapter(fragmentManager!!)
        viewPager_onboardingContainer.adapter = onboardingPagesAdapter
        model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.NEXT)
        viewPager_onboardingContainer.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                if (position == onboardingPagesAdapter.count-1) {
                    model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.DONE)
                } else {
                    model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.NEXT)
                }

                when (position) {
                    0 -> {
                        backButtonIcon_onboardingContainer.visibility = View.GONE
                        backButtonText_onboardingContainer.visibility = View.GONE
                        model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.NEXT)
                    }
                    onboardingPagesAdapter.count-1 -> {
                        model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.DONE)
                        backButtonIcon_onboardingContainer.visibility = View.VISIBLE
                        backButtonText_onboardingContainer.visibility = View.VISIBLE
                    }
                    else -> {
                        model.setForwardButtonState(OnboardingContainerViewModel.Companion.ForwardButtonState.NEXT)
                        backButtonIcon_onboardingContainer.visibility = View.VISIBLE
                        backButtonText_onboardingContainer.visibility = View.VISIBLE
                    }
                }
            }
        })
        // todo: use a textview drawable here
        backButtonText_onboardingContainer.setOnClickListener { onBackPressed() }
        backButtonIcon_onboardingContainer.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed(): Boolean {
        // if we're on the first page, swallow the back press with no action taken
        if (viewPager_onboardingContainer.currentItem > 0) {
            viewPager_onboardingContainer.setCurrentItem(viewPager_onboardingContainer.currentItem - 1, true)
        }
        return true
    }

    private fun updateForwardButton(state: OnboardingContainerViewModel.Companion.ForwardButtonState) {
        val forwardButtonClickAction: View.OnClickListener = when (state) {
            OnboardingContainerViewModel.Companion.ForwardButtonState.NEXT -> {
                nextButtonClickListener
            }
            OnboardingContainerViewModel.Companion.ForwardButtonState.DONE -> {
                doneButtonClickListener
            }
        }
        nextButton_onboardingContainer.setOnClickListener(forwardButtonClickAction)
        nextButton_onboardingContainer.text = getString(state.text)
    }

    private val nextButtonClickListener = View.OnClickListener {
        val currentItem = viewPager_onboardingContainer.currentItem
        viewPager_onboardingContainer.setCurrentItem(currentItem + 1, true)
    }

    private val doneButtonClickListener = View.OnClickListener {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putBoolean(getString(R.string.onboarding_viewed_key), true)
                apply()
            }
        }
        Navigation.findNavController(view!!).navigate(R.id.homeFragment)
    }

    private inner class ScreenSlidePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int = onboardingPages.size

        override fun getItem(position: Int): Fragment {
            return onboardingPages[position]
        }
    }
}