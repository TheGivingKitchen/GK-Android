package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.firebaseStorageUrl

class SafetynetViewModel : ViewModel() {
    companion object {
        const val safetynetDataUrl = firebaseStorageUrl + "safetyNet/safetyNet.json"
    }
}