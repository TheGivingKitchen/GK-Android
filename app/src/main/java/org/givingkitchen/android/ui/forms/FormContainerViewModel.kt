package org.givingkitchen.android.ui.forms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import org.givingkitchen.android.R

class FormContainerViewModel : ViewModel() {
    companion object {
        enum class ForwardButtonState(@StringRes val text: Int) {
            NEXT(R.string.forms_questions_next),
            SUBMIT(R.string.forms_questions_submit)
        }
    }

    private var forwardButtonState: MutableLiveData<ForwardButtonState> = MutableLiveData()

    fun getForwardButtonState(): LiveData<ForwardButtonState> {
        return forwardButtonState
    }

    fun setForwardButtonState(data: ForwardButtonState) {
        forwardButtonState.value = data
    }
}