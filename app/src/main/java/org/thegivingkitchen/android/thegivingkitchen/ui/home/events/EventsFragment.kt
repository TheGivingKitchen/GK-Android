package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import org.thegivingkitchen.android.thegivingkitchen.R

class EventsFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }
}