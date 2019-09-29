package org.givingkitchen.android.ui.homescreen.resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvider
import org.givingkitchen.android.util.Constants

class ResourcesViewModel : ViewModel() {
    companion object {
        const val safetynetLearnMoreURL = "${Constants.givingKitchenUrl}/"
        const val resourcesDataUrl =  "${Constants.firebaseStorageUrl}/safetyNet/safetyNet.json"
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