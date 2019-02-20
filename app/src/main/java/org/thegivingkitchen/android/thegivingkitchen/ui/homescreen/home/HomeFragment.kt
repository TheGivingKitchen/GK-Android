package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.about.AboutFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance.AssistanceFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events.EventsFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.give.GiveFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.safetynettab.SafetynetFragment

class HomeFragment: Fragment()  {
    // todo: onboarding
    // todo: splash screen
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
                // todo: log error here
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

        /* activity?.supportFragmentManager!!
                .beginTransaction()
                .replace(R.id.fragmentContainer_home, homeSection)
                .addToBackStack(null)
                .commit()*/
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