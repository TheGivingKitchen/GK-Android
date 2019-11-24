package org.givingkitchen.android.ui.homescreen.events

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import org.givingkitchen.android.util.Constants.givingKitchenUrl
import java.io.ByteArrayInputStream
import java.io.IOException
import android.os.Looper



class EventsViewModel : ViewModel() {
    companion object {
        const val eventsLearnMoreURL = "$givingKitchenUrl/events"
        private const val eventsDataURL = "$givingKitchenUrl/events-calendar?format=rss"
    }

    private var currentEventsList: MutableLiveData<List<Event>> = MutableLiveData()
    private var progressBarVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getCurrentEventsList(): LiveData<List<Event>> {
        return currentEventsList
    }

    fun setCurrentEventsList(data: List<Event>) {
        currentEventsList.value = data
    }

    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisible
    }

    fun setProgressBarVisibility(visibility: Boolean) {
        progressBarVisible.value = visibility
    }

    fun loadEvents() {
        setProgressBarVisibility(true)

        val uiHandler = Handler(Looper.getMainLooper())
        OkHttpClient().newCall(Request.Builder().url(eventsDataURL).build()).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                uiHandler.post {
                    setProgressBarVisibility(false)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                uiHandler.post {
                    setProgressBarVisibility(false)

                    if (response.isSuccessful) {
                        val xml = response.body()?.string()
                        val data = XmlParser().parse(ByteArrayInputStream(xml?.toByteArray(Charsets.UTF_8)))
                        setCurrentEventsList(data)
                    }
                }
            }
        })
    }
}