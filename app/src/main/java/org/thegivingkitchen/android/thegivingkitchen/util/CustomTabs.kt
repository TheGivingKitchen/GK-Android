package org.thegivingkitchen.android.thegivingkitchen.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import org.thegivingkitchen.android.thegivingkitchen.R

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