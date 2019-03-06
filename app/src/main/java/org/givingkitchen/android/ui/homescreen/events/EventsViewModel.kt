package org.givingkitchen.android.ui.homescreen.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.givingkitchen.android.util.Constants.givingKitchenUrl

class EventsViewModel : ViewModel() {
    companion object {
        const val eventsLearnMoreURL = "$givingKitchenUrl/events"
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