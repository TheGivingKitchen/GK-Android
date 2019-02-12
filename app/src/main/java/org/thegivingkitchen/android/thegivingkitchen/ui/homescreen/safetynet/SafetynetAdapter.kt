package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class SafetynetAdapter(var items: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SERVICE_PROVIDER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                VIEW_TYPE_HEADER
            }
            else -> {
                VIEW_TYPE_SERVICE_PROVIDER
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_safetynet_header, parent, false))
            }
            else -> {
                SocialServiceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SocialServiceProviderViewHolder) {
            holder.bind(items[position] as SocialServiceProvider)
        }
    }

    override fun getItemCount() = items.size
}

class SocialServiceProviderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(socialServiceProvider: SocialServiceProvider) {
        view.findViewById<TextView>(R.id.title_SafetynetRecycler).setTextIfItExists(socialServiceProvider.name)
        view.findViewById<TextView>(R.id.category_SafetynetRecycler).setTextIfItExists(socialServiceProvider.category)
        view.findViewById<TextView>(R.id.address_SafetynetRecycler).setTextIfItExists(socialServiceProvider.address)
        view.findViewById<TextView>(R.id.phone_SafetynetRecycler).setTextIfItExists(socialServiceProvider.phone)
        view.findViewById<TextView>(R.id.description_SafetynetRecycler).setTextIfItExists(socialServiceProvider.description)

        val countiesServedText = socialServiceProvider.countiesServed
        val countiesServedTextView = view.findViewById<TextView>(R.id.counties_SafetynetRecycler)
        if (countiesServedText.isNullOrBlank()) {
            countiesServedTextView.visibility = View.GONE
        } else {
            countiesServedTextView.text = view.context.getString(R.string.safetynet_tab_counties_served, countiesServedText)
        }
    }
}

class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}
