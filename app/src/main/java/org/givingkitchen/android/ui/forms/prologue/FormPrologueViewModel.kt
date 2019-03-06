package org.givingkitchen.android.ui.forms.prologue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.givingkitchen.android.ui.forms.Form

class FormPrologueViewModel: ViewModel() {
    private var currentJson: MutableLiveData<Form> = MutableLiveData()
    private var progressBarVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentJson(): LiveData<Form> {
        return currentJson
    }

    fun setCurrentJson(data: Form) {
        currentJson.value = data
    }

    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisible
    }

    fun setProgressBarVisibility(visibility: Boolean) {
        progressBarVisible.value = visibility
    }
}