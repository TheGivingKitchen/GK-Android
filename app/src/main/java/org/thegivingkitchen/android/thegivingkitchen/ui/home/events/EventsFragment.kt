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
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs
import java.io.ByteArrayInputStream
import java.io.IOException

class EventsFragment : Fragment() {
    companion object {
        private const val learnMoreURL = "$givingKitchenUrl/events/"
        private const val eventsDataURL = "$givingKitchenUrl/events-calendar?format=rss"
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
            response_eventsTab.text = StackOverflowXmlParser().parse(ByteArrayInputStream(result?.toByteArray(Charsets.UTF_8))).toString()
        }

        @Throws(IOException::class)
        fun getData(url: String): String? {
            val request = Request.Builder()
                    .url(url)
                    .build()

            val response = httpClient.newCall(request).execute()
            val responseString = response.body()?.string()

            return responseString
        }
    }
}
