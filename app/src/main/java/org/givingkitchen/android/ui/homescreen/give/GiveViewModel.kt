package org.givingkitchen.android.ui.homescreen.give

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.annotation.StringRes
import org.givingkitchen.android.R
import org.givingkitchen.android.util.Constants.firebaseStorageUrl
import org.givingkitchen.android.util.Constants.givingKitchenUrl

class GiveViewModel: ViewModel() {
    data class DonationExample(val drawable: Int, val amount: String, @StringRes val description: Int)

    companion object {
        const val giveLearnMoreURL = "$givingKitchenUrl/support/"
        const val oneTimeDonationURL = "https://connect.clickandpledge.com/w/Form/d00e52d7-f298-4d35-8be9-05fd93d3194a"
        const val recurringDonationURL = "https://connect.clickandpledge.com/w/Form/a4eb8d47-b792-4285-8ef9-24c353715cd7"
        const val volunteerSignupUrl = "$firebaseStorageUrl/forms/volunteerSignup.json"
        const val joinOurForcesUrl = "$firebaseStorageUrl/forms/joinourforces.json"
        const val stabilityNetworkPartnerUrl = "$firebaseStorageUrl/forms/stabilitynetworkpartner.json"
    }

    private var donationExamplesIndex = 0
    private var currentDonationExample: MutableLiveData<DonationExample> = MutableLiveData()
    private val donationExamples = listOf(
            DonationExample(R.drawable.ic_donation_joy, "$"+5, R.string.give_tab_examples_five),
            DonationExample(R.drawable.ic_donation_bird, "$"+25, R.string.give_tab_examples_late_fee),
            DonationExample(R.drawable.ic_donation_water, "$"+50, R.string.give_tab_examples_water_bill),
            DonationExample(R.drawable.ic_donation_power, "$"+100, R.string.give_tab_examples_power_bill),
            DonationExample(R.drawable.ic_donation_gas, "$"+150, R.string.give_tab_examples_gas_bill),
            DonationExample(R.drawable.ic_donation_housing, "$"+500, R.string.give_tab_examples_housing),
            DonationExample(R.drawable.ic_donation_sh, "$"+1800, R.string.give_tab_examples_total_grant)
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