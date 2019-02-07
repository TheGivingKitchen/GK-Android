package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events.EventsFragment

class HomeViewModel : ViewModel() {

    private var currentFragment: MutableLiveData<Fragment>? = null

    fun getCurrentFragment(): LiveData<Fragment> {
        if (currentFragment == null) {
            currentFragment = MutableLiveData()
            currentFragment!!.value = EventsFragment()
        }
        return currentFragment!!
    }

    fun setCurrentFragment(fragment: Fragment) {
        if (currentFragment == null) {
            currentFragment = MutableLiveData()
        }
        currentFragment!!.value = fragment
    }

}