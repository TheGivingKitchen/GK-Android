package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events.EventsFragment

class HomeViewModel : ViewModel() {

    private var currentFragment: MutableLiveData<HomeSection> = MutableLiveData()

    fun getCurrentFragment(): LiveData<HomeSection> {
        return currentFragment
    }

    fun setCurrentFragment(fragment: HomeSection) {
        currentFragment.value = fragment
    }
}