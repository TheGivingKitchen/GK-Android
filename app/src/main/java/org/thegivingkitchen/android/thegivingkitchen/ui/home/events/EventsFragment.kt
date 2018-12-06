package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs
import java.io.IOException

class EventsFragment : Fragment() {
    // todo: ask about link to volunteer form in the screen on iOS

    companion object {
        private const val learnMoreURL = "https://thegivingkitchen.org/events/"
        private const val eventsDataURL = "https://thegivingkitchen.org/events-calendar?format=rss"
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
            progressBar_eventsTab.visibility = View.GONE
            response_eventsTab.text = result
        }

        @Throws(IOException::class)
        fun getData(url: String): String? {
            val request = Request.Builder()
                    .url(url)
                    .build()

            val response = httpClient.newCall(request).execute()
            return response.body()?.string()
        }
    }
}