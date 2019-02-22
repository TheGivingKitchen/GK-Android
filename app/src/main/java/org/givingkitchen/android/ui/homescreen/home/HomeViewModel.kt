package org.givingkitchen.android.ui.homescreen.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var currentFragment: MutableLiveData<HomeSection> = MutableLiveData()

    fun getCurrentFragment(): LiveData<HomeSection> {
        return currentFragment
    }

    fun setCurrentFragment(fragment: HomeSection) {
        currentFragment.value = fragment
    }
}