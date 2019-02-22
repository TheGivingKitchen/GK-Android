package org.givingkitchen.android.ui.forms.prologue

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
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