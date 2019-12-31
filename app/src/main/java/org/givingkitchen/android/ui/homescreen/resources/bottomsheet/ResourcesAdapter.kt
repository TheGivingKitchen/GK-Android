package org.givingkitchen.android.ui.homescreen.resources.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_social_service_provider.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.resources.ResourceCategory
import org.givingkitchen.android.ui.homescreen.resources.ResourceProvider
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.setTextIfItExists

class ResourcesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val resourceProviderClicks: PublishSubject<ResourceProvider> = PublishSubject.create()
    private var categoryFilteredItems: List<ResourceProvider> = listOf()
    private var items: List<ResourceProvider> = listOf()
    var currentCategoryFilters = ResourceCategory.resourceCategories.map { it.title }.toSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ResourceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false), resourceProviderClicks)

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceProviderViewHolder) {
            holder.bind(items[position])
        }
    }

    fun resourceProviderClicks(): Observable<ResourceProvider> = resourceProviderClicks

    fun searchWithinCategories(searchText: String): List<ResourceProvider> {
        items = if (searchText.isEmpty()) {
            categoryFilteredItems
        } else {
            categoryFilteredItems.filter {
                searchFields(it.description, searchText)
                        || searchFields(it.address, searchText)
                        || searchFields(it.category, searchText)
                        || searchFields(it.contactName, searchText)
                        || searchFields(it.countiesServed, searchText)
                        || searchFields(it.name, searchText)
                        || searchFields(it.phone, searchText)
                        || searchFields(it.website, searchText)
            }
        }
        notifyDataSetChanged()
        return items
    }

    /* Pass in null for categories to filter to all categories */
    fun filterToCategories(categories: List<String>?, searchQuery: String, allItems: List<ResourceProvider>): List<ResourceProvider> {
        if (categories == null) {
            currentCategoryFilters = ResourceCategory.resourceCategories.map { it.title }.toSet()
            items = allItems
        } else {
            currentCategoryFilters = categories.toSet()
            items = allItems.filter { it.category != null && currentCategoryFilters.contains(it.category) }
        }
        categoryFilteredItems = items
        searchWithinCategories(searchQuery)
        return items
    }

    private fun searchFields(responseString: String?, searchText: String): Boolean {
        return responseString != null && responseString.toLowerCase(Constants.rootLocale).contains(searchText.toLowerCase(Constants.rootLocale))
    }
}

class ResourceProviderViewHolder(override val containerView: View, private val clicks: PublishSubject<ResourceProvider>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(resourceProvider: ResourceProvider) {
        title_SafetynetRecycler.setTextIfItExists(resourceProvider.name)
        category_SafetynetRecycler.setTextIfItExists(resourceProvider.category)
        description_SafetynetRecycler.setTextIfItExists(resourceProvider.description)
        containerView.setOnClickListener { clicks.onNext(resourceProvider) }
    }
}