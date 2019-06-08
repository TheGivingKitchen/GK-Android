package org.givingkitchen.android

import android.app.Application
import org.givingkitchen.android.analytics.Analytics

class GivingKitchenApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Analytics.init(this)
    }
}