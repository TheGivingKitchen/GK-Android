package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.safetynettab

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.util.Constants
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.firebaseStorageUrl

class SafetynetViewModel : ViewModel() {
    companion object {
        const val safetynetLearnMoreURL = "${Constants.givingKitchenUrl}/"
        const val safetynetDataUrl =  "$firebaseStorageUrl/safetyNet/safetyNet.json"
    }

    private var currentJson: MutableLiveData<List<SocialServiceProvider>> = MutableLiveData()
    private var progressBarVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentJson(): LiveData<List<SocialServiceProvider>> {
        return currentJson
    }

    fun setCurrentJson(data: List<SocialServiceProvider>) {
        currentJson.value = data
    }

    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisible
    }

    fun setProgressBarVisibility(visibility: Boolean) {
        progressBarVisible.value = visibility
    }
}