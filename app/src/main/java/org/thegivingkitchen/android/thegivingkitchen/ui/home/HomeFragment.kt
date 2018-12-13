package org.thegivingkitchen.android.thegivingkitchen.ui.home

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.home.assistance.AssistanceFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.events.EventsFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.give.GiveFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet.SafetynetFragment

class HomeFragment: Fragment()  {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNav_home.setOnNavigationItemSelectedListener(navListener)
        loadFragment(getSelectedFragment(bottomNav_home.selectedItemId))
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        loadFragment(getSelectedFragment(it.itemId))
        true
    }

    private fun loadFragment(fragment: Fragment) {
        activity?.supportFragmentManager!!
                .beginTransaction()
                .replace(R.id.fragmentContainer_home, fragment)
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