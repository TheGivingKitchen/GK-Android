package org.thegivingkitchen.android.thegivingkitchen.ui.home.events

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

class EventsAdapter(var items: List<Event>, val fragment: Fragment) : RecyclerView.Adapter<EventViewHolder>(){
    // todo: make more viewholders for the top cells so that they scroll with the events cells

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_event, parent, false), fragment)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class EventViewHolder(val view: View, val fragment: Fragment) : RecyclerView.ViewHolder(view) {
    fun bind(event: Event) {
        view.findViewById<TextView>(R.id.title_EventsRecycler).setTextIfItExists(event.title)
        view.findViewById<TextView>(R.id.description_EventsRecycler).setTextIfItExists(event.subtitle?.replace("\n", ""))
        setPicture(event.picUrl, R.id.image_EventsRecycler)
    }

    private fun setPicture(url: String?, @IdRes id: Int) {
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
