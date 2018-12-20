package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class EventsViewModel : ViewModel() {
    private var currentEventsList: MutableLiveData<List<Event>> = MutableLiveData()

    fun getCurrentEventsList(): LiveData<List<Event>> {
        return currentEventsList
    }

    fun setCurrentEventsList(data: List<Event>) {
        currentEventsList.value = data
    }
}