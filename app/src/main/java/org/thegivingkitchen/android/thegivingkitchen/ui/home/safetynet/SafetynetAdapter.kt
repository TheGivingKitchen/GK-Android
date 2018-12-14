package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.thegivingkitchen.android.thegivingkitchen.R

class SafetynetAdapter(var items: List<SocialServiceProvider>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}


class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(socialServiceProvider: SocialServiceProvider) {
        setTextIfItExists(R.id.title_SafetynetRecycler, socialServiceProvider.name)
        setTextIfItExists(R.id.category_SafetynetRecycler, socialServiceProvider.category)
        setTextIfItExists(R.id.address_SafetynetRecycler, socialServiceProvider.address)
        setTextIfItExists(R.id.phone_SafetynetRecycler, socialServiceProvider.phone)
        setTextIfItExists(R.id.description_SafetynetRecycler, socialServiceProvider.description)
        setTextIfItExists(R.id.counties_SafetynetRecycler, view.context.getString(R.string.safetynet_tab_counties_served, socialServiceProvider.countiesServed))
    }

    private fun setTextIfItExists(@IdRes id: Int, text: String?) {
        val view = view.findViewById<TextView>(id)
        if (text.isNullOrBlank()) {
            view.visibility = View.GONE
        } else {
            view.text = text
        }
    }
}

