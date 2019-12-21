package org.givingkitchen.android.ui.homescreen.resources.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import org.givingkitchen.android.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_resource_category.*
import org.givingkitchen.android.ui.homescreen.resources.ResourceCategory

class ResourceCategoriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val resourceCategoryClicks: PublishSubject<ResourceCategory> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ResourceCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_resource_category, parent, false), resourceCategoryClicks)

    override fun getItemCount(): Int {
        return ResourceCategory.resourceCategories.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceCategoryViewHolder) {
            holder.bind(ResourceCategory.resourceCategories[position])
        }
    }

    fun resourceCategoryClicks(): Observable<ResourceCategory> = resourceCategoryClicks
}

class ResourceCategoryViewHolder(override val containerView: View, private val clicks: PublishSubject<ResourceCategory>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(resourceCategory: ResourceCategory) {
        title_resourceCategory.text = resourceCategory.title
    }
}