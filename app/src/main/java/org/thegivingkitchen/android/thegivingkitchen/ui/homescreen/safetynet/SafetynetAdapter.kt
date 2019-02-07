package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class SafetynetAdapter(var items: List<SocialServiceProvider>) : RecyclerView.Adapter<SocialServiceProviderViewHolder>() {
    // todo: make more viewholders for the top cells so that they scroll with the safetynet provider cells

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialServiceProviderViewHolder {
        return SocialServiceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false))
    }

    override fun onBindViewHolder(holder: SocialServiceProviderViewHolder, position: Int) {
        holder.bind(items[position])
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
