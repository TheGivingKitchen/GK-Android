package org.givingkitchen.android.ui.homescreen.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
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
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetFragment

class HomeFragment: Fragment()  {
    // todo: user analytics
    private lateinit var model: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        model.getCurrentFragment().observe(this, Observer<HomeSection> { liveData ->
            loadFragment(liveData!!)
        })
        model.setCurrentFragment(HomeSection.ABOUT)
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNav_home.setOnNavigationItemSelectedListener(navListener)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        val onboardingViewed = sharedPref.getBoolean(getString(R.string.onboarding_viewed_key), false)
        if (!onboardingViewed) {
            Navigation.findNavController(view).navigate(R.id.onboardingContainerFragment)
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
            R.id.safetynetFragment -> {
                HomeSection.SAFETYNET
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
            }

            detachCurrentFragment()
            childFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer_home, fragment, tag)
                    .commit()
        } else {
            detachCurrentFragment()
            childFragmentManager
                    .beginTransaction()
                    .attach(fragment)
                    .commit()
        }
    }

    private fun getCurrentFragment(): Fragment? = childFragmentManager.findFragmentById(R.id.fragmentContainer_home)

    private fun detachCurrentFragment() {
        val currentFragment = getCurrentFragment()
        if (currentFragment != null) {
            childFragmentManager
                    .beginTransaction()
                    .detach(currentFragment)
                    .commit()
        }
    }
}