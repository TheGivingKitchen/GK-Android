package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.StringRes
import org.thegivingkitchen.android.thegivingkitchen.R

class QuestionsContainerViewModel : ViewModel() {
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