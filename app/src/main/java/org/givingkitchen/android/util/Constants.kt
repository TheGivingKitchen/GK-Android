package org.givingkitchen.android.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.givingkitchen.android.R
import java.util.*

object Constants {
    const val firebaseStorageUrl = "gs://thegivingkitchen-cdd28.appspot.com"
    const val givingKitchenUrl = "https://thegivingkitchen.org"
    const val formShareWufooUrl = "https://thegivingkitchen.wufoo.com/forms/"
    const val formsArg = "formsUrl"
    const val donePageArg = "formsDonePage"
    const val httpUnauthorizedError = 401
    val rootLocale = Locale.ROOT
}

enum class DonePage(@DrawableRes val drawable: Int, @StringRes val title: Int, @StringRes val description: Int, @StringRes val buttonText: Int, @StringRes val shareString: Int? = null) {
    ASSISTANCE_INQUIRY(R.drawable.assistance_header, R.string.form_done_header_thanks_fullstop, R.string.form_done_description_assistance, R.string.form_done_button_done),
    STABILITY_NETWORK(R.drawable.give_header, R.string.form_done_header_thanks_exclamation, R.string.form_done_description_stability_network, R.string.form_done_button_done, R.string.form_done_share_url_stability_network),
    VOLUNTEER(R.drawable.give_header, R.string.form_done_header_welcome, R.string.form_done_description_volunteer, R.string.form_done_button_done, R.string.form_done_share_url_volunteer),
    DEFAULT(R.drawable.assistance_header, R.string.form_done_header_thanks_fullstop, R.string.form_done_description_default, R.string.form_done_button_done)
}
