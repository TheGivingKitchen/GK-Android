package org.givingkitchen.android.ui.homescreen.give

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import org.givingkitchen.android.R
import org.givingkitchen.android.util.Constants.firebaseStorageUrl
import org.givingkitchen.android.util.Constants.givingKitchenUrl

class GiveViewModel : ViewModel() {
    data class DonationExample(val drawable: Int, val amount: Int, @StringRes val description: Int)

    companion object {
        const val giveLearnMoreURL = "$givingKitchenUrl/support/"
        const val creditCardDonationURL = "https://connect.clickandpledge.com/w/Form/d11bff52-0cd0-44d8-9403-465614e4f342"
        const val recurringDonationURL = "https://connect.clickandpledge.com/w/Form/40b3de1f-fa46-4735-874f-c152e272620e"
        const val volunteerSignupUrl = "$firebaseStorageUrl/forms/volunteerSignup.json"
        const val joinOurForcesUrl = "$firebaseStorageUrl/forms/joinourforces.json"
        const val stabilityNetworkPartnerUrl = "$firebaseStorageUrl/forms/stabilitynetworkpartner.json"
    }

    private var donationExamplesIndex = 0
    private var currentDonationExample: MutableLiveData<DonationExample> = MutableLiveData()
    private val donationExamples = listOf(
            DonationExample(R.drawable.ic_donation_joy, 25, R.string.give_tab_examples_late_fee),
            DonationExample(R.drawable.ic_donation_joy, 50, R.string.give_tab_examples_water_bill),
            DonationExample(R.drawable.ic_donation_joy, 100, R.string.give_tab_examples_power_bill),
            DonationExample(R.drawable.ic_donation_joy, 150, R.string.give_tab_examples_gas_bill),
            DonationExample(R.drawable.ic_donation_joy, 500, R.string.give_tab_examples_housing),
            DonationExample(R.drawable.ic_donation_joy, 1800, R.string.give_tab_examples_total_grant)
    )

    init {
        currentDonationExample.value = donationExamples[donationExamplesIndex]
    }

    fun getDonationExample(): LiveData<DonationExample> {
        return currentDonationExample
    }

    fun nextDonationExample() {
        donationExamplesIndex++
        if (donationExamplesIndex > donationExamples.size-1) {
            donationExamplesIndex = 0
        }

        currentDonationExample.value = donationExamples[donationExamplesIndex]
    }
}