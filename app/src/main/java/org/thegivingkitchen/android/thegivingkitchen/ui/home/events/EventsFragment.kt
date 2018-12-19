package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.util.Xml
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_events.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

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

data class Event(val title: String?, val picUrl: String?, val description: String?)


// We don't use namespaces
private val ns: String? = null

class StackOverflowXmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): List<Event> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): List<Event> {
        val entries = mutableListOf<Event>()

        parser.require(XmlPullParser.START_TAG, ns, "rss")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "channel") {
                parser.require(XmlPullParser.START_TAG, ns, "channel")
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    if (parser.name == "item") {
                        entries.add(readItem(parser))
                    } else {
                        skip(parser)
                    }
                }
            }
        }
        return entries
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }


    // Parses the contents of an item. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readItem(parser: XmlPullParser): Event {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var title: String? = null
        var picUrl: String? = null
        var description: String? = null
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTitle(parser)
                // "media:content" -> picUrl = readLink(parser)
                "description" -> description = readSummary(parser)
                else -> skip(parser)
            }
        }
        return Event(title, picUrl, description)
    }

    // Processes title tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    // Processes link tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        var link = ""
        parser.require(XmlPullParser.START_TAG, ns, "media:content")
        val tag = parser.name
        val relType = parser.getAttributeValue(null, "type")
        if (tag == "media:content") {
            if (relType == "image/jpeg") {
                link = parser.getAttributeValue(null, "url")
                parser.nextTag()
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:content")
        return link
    }

    // Processes summary tags in the feed.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readSummary(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        return summary
    }

    // For the tags title and summary, extracts their text values.
    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
}