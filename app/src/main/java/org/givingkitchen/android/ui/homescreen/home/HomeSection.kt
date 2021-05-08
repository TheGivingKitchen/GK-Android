package org.givingkitchen.android.ui.homescreen.home

import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.about.AboutFragment
import org.givingkitchen.android.ui.homescreen.assistance.AssistanceFragment
import org.givingkitchen.android.ui.homescreen.events.EventsFragment
import org.givingkitchen.android.ui.homescreen.give.GiveFragment
import org.givingkitchen.android.ui.homescreen.resources.ResourcesFragment

enum class HomeSection(val id: Int, val screenName: String, val bottomNavId: Int, val index: Int) {
    ABOUT(R.id.aboutFragment, AboutFragment::class.java.simpleName, R.id.aboutFragment, 0),
//    EVENTS(R.id.eventsFragment, EventsFragment::class.java.simpleName, R.id.eventsFragment, 1),
    ASSISTANCE(R.id.assistanceFragment, AssistanceFragment::class.java.simpleName, R.id.assistanceFragment, 1),
    RESOURCES(R.id.resourcesFragment, ResourcesFragment::class.java.simpleName, R.id.resourcesFragment, 2),
    GIVE(R.id.giveFragment, GiveFragment::class.java.simpleName, R.id.giveFragment, 3);

    companion object {
        fun getFragment(name: String): HomeSection {
            return when (name) {
                "AboutFragment" -> ABOUT
//                "EventsFragment" -> EVENTS
                "AssistanceFragment" -> ASSISTANCE
                "GiveFragment" -> GIVE
                "ResourcesFragment" -> RESOURCES
                else -> ABOUT
            }
        }
    }
}