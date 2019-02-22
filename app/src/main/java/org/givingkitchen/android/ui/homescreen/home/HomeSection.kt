package org.givingkitchen.android.ui.homescreen.home

import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.about.AboutFragment
import org.givingkitchen.android.ui.homescreen.assistance.AssistanceFragment
import org.givingkitchen.android.ui.homescreen.events.EventsFragment
import org.givingkitchen.android.ui.homescreen.give.GiveFragment
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetFragment

enum class HomeSection(val id: Int, val screenName: String) {
    ABOUT(R.id.aboutFragment, AboutFragment::class.java.simpleName),
    EVENTS(R.id.eventsFragment, EventsFragment::class.java.simpleName),
    ASSISTANCE(R.id.assistanceFragment, AssistanceFragment::class.java.simpleName),
    GIVE(R.id.giveFragment, GiveFragment::class.java.simpleName),
    SAFETYNET(R.id.safetynetFragment, SafetynetFragment::class.java.simpleName)
}