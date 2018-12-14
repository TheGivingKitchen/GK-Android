package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.firebaseStorageUrl

class SafetynetViewModel : ViewModel() {
    companion object {
        const val safetynetDataUrl = firebaseStorageUrl + "safetyNet/safetyNet.json"
    }

    private var currentJson: MutableLiveData<List<SocialServiceProvider>> = MutableLiveData()

    fun getCurrentJson(): LiveData<List<SocialServiceProvider>> {
        return currentJson
    }

    fun setCurrentJson(data: List<SocialServiceProvider>) {
        currentJson.value = data
    }
}