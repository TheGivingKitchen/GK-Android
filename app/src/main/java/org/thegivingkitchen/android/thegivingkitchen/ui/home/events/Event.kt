package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

open class EventRecyclerItem(var title: String?, var subtitle: String?)

class Event(title: String?, subtitle: String?, var picUrl: String? = null): EventRecyclerItem(title, subtitle)
