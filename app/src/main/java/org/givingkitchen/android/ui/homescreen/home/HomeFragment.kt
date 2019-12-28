package org.givingkitchen.android.ui.homescreen.home

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.Nullable
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_home.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.about.AboutFragment
import org.givingkitchen.android.ui.homescreen.assistance.AssistanceFragment
import org.givingkitchen.android.ui.homescreen.events.EventsFragment
import org.givingkitchen.android.ui.homescreen.give.GiveFragment
import org.givingkitchen.android.ui.homescreen.resources.ResourcesFragment
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetFragment
import org.givingkitchen.android.util.FeatureFlags
import org.givingkitchen.android.util.FragmentBackPressedListener
import org.givingkitchen.android.util.getGivingKitchenSharedPreferences

class HomeFragment: Fragment(), FragmentBackPressedListener  {
    private lateinit var model: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        model.getCurrentFragment().observe(this, Observer<HomeSection> { liveData ->
            loadFragment(liveData!!)
        })
        val savedHomeTab = activity.getGivingKitchenSharedPreferences()?.getString(getString(R.string.home_tab_key), null)
        if (savedHomeTab != null) {
            model.setCurrentFragment(HomeSection.getFragment(savedHomeTab))
        }
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNav_home.setOnNavigationItemSelectedListener(navListener)

        val onboardingViewed = activity.getGivingKitchenSharedPreferences()?.getBoolean(getString(R.string.onboarding_viewed_key), false) ?: return
        if (!onboardingViewed) {
            Navigation.findNavController(view).navigate(R.id.onboardingContainerFragment)
        }
    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        activity.getGivingKitchenSharedPreferences()?.let {
            with (it.edit()) {
                putString(getString(R.string.home_tab_key), model.getCurrentFragment().value!!.screenName)
                apply()
            }
        }
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        val selectedHomeSection: HomeSection = when (it.itemId) {
            R.id.aboutFragment -> {
                HomeSection.ABOUT
            }
            R.id.eventsFragment -> {
                HomeSection.EVENTS
            }
            R.id.assistanceFragment -> {
                HomeSection.ASSISTANCE
            }
            R.id.giveFragment -> {
                HomeSection.GIVE
            }
            R.id.resourcesFragment -> {
                if (FeatureFlags.newResourcesTab) {
                    HomeSection.RESOURCES
                } else {
                    HomeSection.SAFETYNET
                }
            }
            else -> {
                Crashlytics.log("User tried to navigate to unexpected home tab: ${it.itemId}")
                HomeSection.ABOUT
            }
        }

        model.setCurrentFragment(selectedHomeSection)
        true
    }

    private fun loadFragment(homeSection: HomeSection) {
        val tag = homeSection.screenName
        var fragment: Fragment? = childFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = when (homeSection) {
                HomeSection.ABOUT -> AboutFragment()
                HomeSection.EVENTS -> EventsFragment()
                HomeSection.ASSISTANCE -> AssistanceFragment()
                HomeSection.GIVE -> GiveFragment()
                HomeSection.SAFETYNET -> SafetynetFragment()
                HomeSection.RESOURCES -> ResourcesFragment()
            }
        }

        childFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer_home, fragment, tag)
                .addToBackStack(null)
                .commit()

        bottomNav_home.menu.getItem(homeSection.index).setChecked(true)
    }
}