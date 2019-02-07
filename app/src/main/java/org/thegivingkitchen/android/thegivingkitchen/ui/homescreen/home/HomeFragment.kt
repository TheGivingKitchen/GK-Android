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
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance.AssistanceFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events.EventsFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.give.GiveFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.SafetynetFragment

class HomeFragment: Fragment()  {
    // todo: onboarding
    // todo: splash screen
    private lateinit var model: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        model.getCurrentFragment().observe(this, Observer<Fragment> { liveData ->
            loadFragment(liveData!!)
        })
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

    fun getRootLayout(): View {
        return rootLayout_home
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        model.setCurrentFragment(getSelectedFragment(it.itemId))
        true
    }

    private fun loadFragment(fragment: Fragment) {
        activity?.supportFragmentManager!!
                .beginTransaction()
                .replace(R.id.fragmentContainer_home, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun getSelectedFragment(item: Int): Fragment {
        return when (item) {
            R.id.eventsFragment -> {
                EventsFragment()
            }
            R.id.assistanceFragment -> {
                AssistanceFragment()
            }
            R.id.giveFragment -> {
                GiveFragment()
            }
            R.id.safetynetFragment -> {
                SafetynetFragment()
            }
            else -> {
                // todo: log error here
                EventsFragment()
            }
        }
    }
}