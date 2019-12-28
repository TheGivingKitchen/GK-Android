package org.givingkitchen.android.ui.homescreen.events

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setTextIfItExists

class EventsAdapter(var items: MutableList<Any>, val fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val learnMoreClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val eventClicks: PublishSubject<Event> = PublishSubject.create()

    companion object {
        const val VIEW_TYPE_EVENT = 0
        const val VIEW_TYPE_HEADER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Event) {
            VIEW_TYPE_EVENT
        } else {
            VIEW_TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_events_header, parent, false), learnMoreClicks)
        } else {
            EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_event, parent, false), eventClicks)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            holder.bind(items[position] as Event, fragment)
        } else {
            (holder as HeaderViewHolder).bind()
        }
    }

    override fun getItemCount() = items.size

    fun learnMoreClicks(): Observable<Boolean> = learnMoreClicks
    fun eventClicks(): Observable<Event> = eventClicks
}

class HeaderViewHolder(val view: View, private val learnMoreClicks: PublishSubject<Boolean>) : RecyclerView.ViewHolder(view) {
    fun bind() {
        view.findViewById<TextView>(R.id.learnMoreButton_eventsTab).setOnClickListener { learnMoreClicks.onNext(false) }
    }
}

class EventViewHolder(val view: View, private val clicks: PublishSubject<Event>) : RecyclerView.ViewHolder(view) {
    fun bind(event: Event, fragment: Fragment) {
        view.findViewById<TextView>(R.id.title_EventsRecycler).setTextIfItExists(event.title)
        view.findViewById<TextView>(R.id.description_EventsRecycler).setTextIfItExists(event.subtitle?.replace("\n", ""))
        val link = event.link
        if (link != null) {
            view.setOnClickListener { clicks.onNext(event) }
        }
        setPicture(event.picUrl, R.id.image_EventsRecycler, fragment)
    }

    private fun setPicture(url: String?, @IdRes id: Int, fragment: Fragment) {
        // url must be https
        var httpsUrl = ""
        if (url != null) {
            httpsUrl = if (url.startsWith("https")) {
                url
            } else {
                url.substring(0, 4) + "s" + url.substring(4)
            }
        }

        Glide.with(fragment)
                .load(httpsUrl)
                .into(view.findViewById(id))
    }
}
