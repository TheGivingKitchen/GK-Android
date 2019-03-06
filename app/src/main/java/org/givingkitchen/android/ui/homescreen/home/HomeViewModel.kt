package org.givingkitchen.android.ui.homescreen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var currentFragment: MutableLiveData<HomeSection> = MutableLiveData()

    fun getCurrentFragment(): LiveData<HomeSection> {
        return currentFragment
    }

    fun setCurrentFragment(fragment: HomeSection) {
        currentFragment.value = fragment
    }
}