package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.firebaseStorageUrl

class SafetynetViewModel : ViewModel() {
    companion object {
        const val safetynetDataUrl = firebaseStorageUrl + "safetyNet/safetyNet.json"
    }

    private var currentJson: MutableLiveData<String> = MutableLiveData()

    fun getCurrentJson(): LiveData<String> {
        return currentJson
    }

    fun setCurrentJson(json: String) {
        currentJson.value = json
    }
}