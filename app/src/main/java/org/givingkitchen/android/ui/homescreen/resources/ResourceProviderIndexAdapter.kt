package org.givingkitchen.android.ui.homescreen.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.safetynet.Header
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvider
import org.givingkitchen.android.util.setTextIfItExists

class ResourceProviderIndexAdapter(var items: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val serviceProviderClicks: PublishSubject<Int> = PublishSubject.create()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SERVICE_PROVIDER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Header) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_SERVICE_PROVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SocialServiceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false), serviceProviderClicks)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SocialServiceProviderViewHolder) {
            holder.bind(items[position] as SocialServiceProvider)
        }
    }

    override fun getItemCount() = items.size
}

class SocialServiceProviderViewHolder(val view: View, private val clicks: PublishSubject<Int>) : RecyclerView.ViewHolder(view) {
    fun bind(socialServiceProvider: SocialServiceProvider) {
        view.findViewById<TextView>(R.id.title_SafetynetRecycler).setTextIfItExists(socialServiceProvider.name)
        view.findViewById<TextView>(R.id.category_SafetynetRecycler).setTextIfItExists(socialServiceProvider.category)
        view.findViewById<TextView>(R.id.description_SafetynetRecycler).setTextIfItExists(socialServiceProvider.description)
        val cellIndex = socialServiceProvider.index
        if (cellIndex != null) {
            view.setOnClickListener { clicks.onNext(cellIndex) }
        }
    }
}
