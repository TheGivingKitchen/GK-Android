package org.givingkitchen.android.ui.onboarding

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.StringRes
import org.givingkitchen.android.R

class OnboardingContainerViewModel : ViewModel() {
    companion object {
        enum class ForwardButtonState(@StringRes val text: Int) {
            NEXT(R.string.onboarding_container_next),
            DONE(R.string.onboarding_container_done)
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