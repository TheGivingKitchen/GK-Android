package org.givingkitchen.android.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import org.givingkitchen.android.R

class CustomTabs {
    companion object {
        fun openCustomTab(context: Context?, url: String) {
            if (context != null) {
                CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(context, R.color.white))
                        .build()
                        .launchUrl(context, Uri.parse(url))
            }
        }
    }
}