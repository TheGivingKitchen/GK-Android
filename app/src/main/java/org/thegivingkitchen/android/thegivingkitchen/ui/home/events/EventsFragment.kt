package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs

class EventsFragment : Fragment() {
    // todo: ask about link to volunteer form in the screen on iOS

    private val learnMoreURL = "https://thegivingkitchen.org/events/"

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learn_more_button_eventsTab.setOnClickListener(learnMoreButtonClickListener)
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, learnMoreURL)
    }
}