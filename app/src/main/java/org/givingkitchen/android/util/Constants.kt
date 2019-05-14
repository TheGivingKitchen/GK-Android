package org.givingkitchen.android.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.givingkitchen.android.R

object Constants {
    const val firebaseStorageUrl = "gs://thegivingkitchen-cdd28.appspot.com"
    const val givingKitchenUrl = "https://thegivingkitchen.org"
    const val formShareWufooUrl = "https://thegivingkitchen.wufoo.com/forms/"
    const val formsArg = "formsUrl"
    const val donePageArg = "formsDonePage"
}

enum class DonePage(@DrawableRes drawable: Int, @StringRes title: Int, @StringRes description: Int, @StringRes buttonText: Int, enableShareButton: Boolean) {
    ASSISTANCE_INQUIRY(R.drawable.assistance_header, R.string.form_done_header_thanks_fullstop, R.string.form_done_description_assistance, R.string.form_done_button_done, false),
    STABILITY_NETWORK(R.drawable.give_header, R.string.form_done_header_thanks_exclamation, R.string.form_done_description_stability_network, R.string.form_done_button_done, false),
    VOLUNTEER(R.drawable.give_header, R.string.form_done_header_welcome, R.string.form_done_description_volunteer, R.string.form_done_button_done, false),
    DEFAULT(R.drawable.assistance_header, R.string.form_done_header_thanks_fullstop, R.string.form_done_description_default, R.string.form_done_button_done, false)
}
