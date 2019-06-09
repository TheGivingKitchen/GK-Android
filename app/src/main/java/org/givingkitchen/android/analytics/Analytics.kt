package org.givingkitchen.android.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Analytics {

    private lateinit var instance: FirebaseAnalytics

    fun init(context: Context) {
        instance = FirebaseAnalytics.getInstance(context)
    }

    fun logEvent(analyticEvent: Events) {
        instance.logEvent(analyticEvent.name, null)
    }

    fun logEvent(analyticEvent: Events, parameters: Map<Parameter, String>) {
        val bundle = Bundle()
        for (entry in parameters) {
            bundle.putString(entry.key.name, entry.value)
        }
        instance.logEvent(analyticEvent.name, bundle)
    }

    fun logLearnedMore(type: String) {
        logEvent(Events.LEARN_MORE_PRESSED, mapOf(Parameter.LEARN_MORE_TYPE to type))
    }
}
