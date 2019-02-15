package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.events

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class EventsAdapter(var items: MutableList<Any>, val fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_events_header, parent, false))
        } else {
            EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_event, parent, false))
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
}

class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(event: Event, fragment: Fragment) {
        view.findViewById<TextView>(R.id.title_EventsRecycler).setTextIfItExists(event.title)
        view.findViewById<TextView>(R.id.description_EventsRecycler).setTextIfItExists(event.subtitle?.replace("\n", ""))
        setPicture(event.picUrl, R.id.image_EventsRecycler, fragment)
    }

    private fun setPicture(url: String?, @IdRes id: Int, fragment: Fragment) {
        // url must be https
        var httpsUrl = ""
        if (url != null) {
            if (url.startsWith("https")) {
                httpsUrl = url
            } else {
                httpsUrl = url.substring(0, 4) + "s" + url.substring(4)
            }
        }

        Glide.with(fragment)
                .load(httpsUrl)
                .into(view.findViewById(id))
    }
}

class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind() { }
}
