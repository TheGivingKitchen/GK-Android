package org.thegivingkitchen.android.thegivingkitchen.ui.forms.prologue

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Form

class FormPrologueViewModel: ViewModel() {
    private var currentJson: MutableLiveData<Form> = MutableLiveData()

    fun getCurrentJson(): LiveData<Form> {
        return currentJson
    }

    fun setCurrentJson(data: Form) {
        currentJson.value = data
    }
}