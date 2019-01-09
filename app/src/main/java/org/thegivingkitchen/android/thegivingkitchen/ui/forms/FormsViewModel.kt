package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class FormsViewModel: ViewModel() {
    private var currentJson: MutableLiveData<String> = MutableLiveData()

    fun getCurrentJson(): LiveData<String> {
        return currentJson
    }

    fun setCurrentJson(data: String) {
        currentJson.value = data
    }
}