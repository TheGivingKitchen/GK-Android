package org.thegivingkitchen.android.thegivingkitchen.ui.home.give

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.StringRes
import org.thegivingkitchen.android.thegivingkitchen.R

class GiveViewModel : ViewModel() {
    data class DonationExample(val amount: Int, @StringRes val description: Int)
    data class DonationExampleLiveData(val amount: Int, @StringRes val description: Int, val leftArrowEnabled: Boolean, val rightArrowEnabled: Boolean)

    val creditCardDonationURL = "https://connect.clickandpledge.com/w/Form/d11bff52-0cd0-44d8-9403-465614e4f342"
    val recurringDonationURL = "https://connect.clickandpledge.com/w/Form/40b3de1f-fa46-4735-874f-c152e272620e"

    private val donationExamples = listOf(
            DonationExample(25, R.string.give_tab_examples_late_fee),
            DonationExample(50, R.string.give_tab_examples_water_bill),
            DonationExample(100, R.string.give_tab_examples_power_bill),
            DonationExample(150, R.string.give_tab_examples_gas_bill),
            DonationExample(500, R.string.give_tab_examples_housing),
            DonationExample(1800, R.string.give_tab_examples_total_grant)
    )

    private var currentDonationExamplesIndex = 0
    private var currentDonationData: MutableLiveData<DonationExampleLiveData>? = null

    fun getCurrentDonationData(): LiveData<DonationExampleLiveData> {
        if (currentDonationData == null) {
            currentDonationData = MutableLiveData()
            val currentDonationExample = donationExamples[0]
            currentDonationData?.value = DonationExampleLiveData(currentDonationExample.amount, currentDonationExample.description, currentDonationExamplesIndex != 0, true)
        }
        return currentDonationData!!
    }

    fun onLeftArrowClicked() {
        if (currentDonationExamplesIndex > 0) {
            val currentDonationExample = donationExamples[--currentDonationExamplesIndex]
            currentDonationData?.value = DonationExampleLiveData(currentDonationExample.amount, currentDonationExample.description, currentDonationExamplesIndex != 0, true)
        }
    }

    fun onRightArrowClicked() {
        if (currentDonationExamplesIndex < donationExamples.size-1) {
            val currentDonationExample = donationExamples[++currentDonationExamplesIndex]
            currentDonationData?.value = DonationExampleLiveData(currentDonationExample.amount, currentDonationExample.description, true, currentDonationExamplesIndex != donationExamples.size-1)
        }
    }
}