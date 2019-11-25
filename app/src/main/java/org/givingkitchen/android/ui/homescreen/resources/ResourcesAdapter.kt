package org.givingkitchen.android.ui.homescreen.resources

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_social_service_provider.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setTextIfItExists

class ResourcesAdapter(var items: MutableList<*>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val resourceProviderClicks: PublishSubject<ResourceProvider> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ResourceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false), resourceProviderClicks)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceProviderViewHolder) {
            holder.bind(items[position] as ResourceProvider)
        }
    }

    fun resourceProviderClicks(): Observable<ResourceProvider> = resourceProviderClicks
}

class ResourceProviderViewHolder(override val containerView: View, private val clicks: PublishSubject<ResourceProvider>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(resourceProvider: ResourceProvider) {
        title_SafetynetRecycler.setTextIfItExists(resourceProvider.name)
        category_SafetynetRecycler.setTextIfItExists(resourceProvider.category)
        description_SafetynetRecycler.setTextIfItExists(resourceProvider.description)
        containerView.setOnClickListener { clicks.onNext(resourceProvider) }
    }
}