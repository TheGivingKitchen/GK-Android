package org.givingkitchen.android.ui.homescreen.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setTextIfItExists

class ResourcesAdapter(var items: MutableList<*>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val serviceProviderClicks: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ResourceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false), serviceProviderClicks)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceProviderViewHolder) {
            holder.bind(items[position] as ResourceProvider)
        }
    }
}

class ResourceProviderViewHolder(val view: View, private val clicks: PublishSubject<Int>) : RecyclerView.ViewHolder(view) {
    fun bind(socialServiceProvider: ResourceProvider) {
        view.findViewById<TextView>(R.id.title_SafetynetRecycler).setTextIfItExists(socialServiceProvider.name)
        view.findViewById<TextView>(R.id.category_SafetynetRecycler).setTextIfItExists(socialServiceProvider.category)
        view.findViewById<TextView>(R.id.description_SafetynetRecycler).setTextIfItExists(socialServiceProvider.description)
        val cellIndex = socialServiceProvider.index
        if (cellIndex != null) {
            view.setOnClickListener { clicks.onNext(cellIndex) }
        }
    }
}