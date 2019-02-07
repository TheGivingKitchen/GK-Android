package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs
import java.io.ByteArrayInputStream
import java.io.IOException

class EventsFragment : Fragment() {
    companion object {
        private const val learnMoreURL = "$givingKitchenUrl/events/"
        private const val eventsDataURL = "$givingKitchenUrl/events-calendar?format=rss"
    }

    private var adapter = EventsAdapter(listOf(), this)
    private lateinit var model: EventsViewModel

    // todo: don't crash the app if a response is not received within 30 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        model.getCurrentEventsList().observe(this, Observer<List<Event>> { liveData ->
            updateEventsList(liveData!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learn_more_button_eventsTab.setOnClickListener(learnMoreButtonClickListener)
        GetEventsTask().execute(eventsDataURL)
        recyclerView_eventsTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_eventsTab.adapter = adapter
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, learnMoreURL)
    }

    inner class GetEventsTask : AsyncTask<String, Void, String>() {
        private val httpClient = OkHttpClient()

        override fun doInBackground(vararg params: String): String? {
            publishProgress()
            return getData(params[0])
        }

        override fun onProgressUpdate(vararg values: Void?) {
            progressBar_eventsTab.visibility = View.VISIBLE
        }

        override fun onPostExecute(result: String?) {
            model.setCurrentEventsList(XmlParser().parse(ByteArrayInputStream(result?.toByteArray(Charsets.UTF_8))))
        }

        @Throws(IOException::class)
        fun getData(url: String): String? {
            return httpClient.newCall(Request.Builder().url(url).build()).execute().body()?.string()
        }
    }

    private fun updateEventsList(data: List<Event>) {
        adapter.items = data
        adapter.notifyDataSetChanged()
        progressBar_eventsTab.visibility = View.GONE
    }
}
