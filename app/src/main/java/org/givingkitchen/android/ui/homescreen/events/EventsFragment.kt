package org.givingkitchen.android.ui.homescreen.events

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_events.*
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Events.*
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.analytics.Parameter.*
import org.givingkitchen.android.ui.homescreen.events.EventsViewModel.Companion.eventsLearnMoreURL
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.CustomTabs
import org.givingkitchen.android.util.DonePage
import org.givingkitchen.android.util.putEnum

class EventsFragment : Fragment() {

    private var adapter = EventsAdapter(mutableListOf(), this)
    private lateinit var model: EventsViewModel

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        model.getCurrentEventsList().observe(this, Observer<List<Event>> { liveData ->
            updateEventsList(liveData!!)
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData!!)
        })
        adapter.learnMoreClicks().subscribe { openLearnMoreLink() }
        adapter.eventClicks().subscribe { goToEventDetails(it) }
        model.loadEvents()
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_eventsTab.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.RecyclerView.VERTICAL, false)
        recyclerView_eventsTab.adapter = adapter
        setupVolunteerBanner()
    }

    private fun openLearnMoreLink() {
        Analytics.logLearnedMore("events_home")
        CustomTabs.openCustomTab(context, eventsLearnMoreURL)
    }

    private fun goToEventDetails(event: Event) {
        val params = mapOf(EVENT_VIEW_DETAILS_EVENT_NAME to (event.title ?: ""))
        Analytics.logEvent(EVENT_VIEW_DETAILS, params)

        CustomTabs.openCustomTab(context, event.link!!)
    }

    private fun setupVolunteerBanner() {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val showBanner = sharedPref?.getBoolean(getString(R.string.show_events_volunteer_banner_key), true) ?: true
        if (showBanner) {
            volunteerBanner_eventsTab.visibility = View.VISIBLE
            volunteerBanner_eventsTab.onTitleClick().subscribe { goToVolunteerForm() }
            volunteerBanner_eventsTab.onCloseClick().subscribe { closeVolunteerBanner() }
        }
    }

    private fun goToVolunteerForm() {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.volunteerSignupUrl)
        args.putEnum(Constants.donePageArg, DonePage.VOLUNTEER)
        Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
    }

    private fun closeVolunteerBanner() {
        volunteerBanner_eventsTab.visibility = View.GONE

        activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)?.let {
            with(it.edit()) {
                putBoolean(getString(R.string.show_events_volunteer_banner_key), false)
                apply()
            }
        }
    }

    private fun updateEventsList(data: List<Event>) {
        val dataMutableList = data.toMutableList<Any>()
        dataMutableList.add(0, Header())
        adapter.items = dataMutableList
        adapter.notifyDataSetChanged()
        model.setProgressBarVisibility(false)
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_eventsTab.visibility = View.VISIBLE
            }
            false -> {
                progressBar_eventsTab.visibility = View.GONE
            }
        }
    }
}
