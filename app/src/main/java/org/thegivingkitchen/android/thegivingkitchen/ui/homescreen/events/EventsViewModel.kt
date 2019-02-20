package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl

class EventsViewModel : ViewModel() {
    companion object {
        const val learnMoreURL = "$givingKitchenUrl/events"
    }

    private var currentEventsList: MutableLiveData<List<Event>> = MutableLiveData()
    private var progressBarVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentEventsList(): LiveData<List<Event>> {
        return currentEventsList
    }

    fun setCurrentEventsList(data: List<Event>) {
        currentEventsList.value = data
    }

    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisible
    }

    fun setProgressBarVisibility(visibility: Boolean) {
        progressBarVisible.value = visibility
    }
}