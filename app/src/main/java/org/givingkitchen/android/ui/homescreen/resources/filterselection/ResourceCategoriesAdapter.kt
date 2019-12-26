package org.givingkitchen.android.ui.homescreen.resources.filterselection

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

class ResourceCategoriesAdapter(val currentlySelectedCategories: Set<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ResourceCategory.resourceCategories.map { ResourceCategoryCell(it, it.title in currentlySelectedCategories) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ResourceCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_resource_category, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceCategoryViewHolder) {
            holder.bind(position)
        }
    }

    fun getSelectedFilters(): List<String> {
        return items.filter { it.selected }.map { it.resourceCategory.title }
    }

    inner class ResourceCategoryViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(position: Int) {
            title_resourceCategory.text = items[position].resourceCategory.title
            checkbox_resourceCategory.isChecked = items[position].selected

            containerView.setOnClickListener {
                checkbox_resourceCategory.toggle()
                items[position].selected = checkbox_resourceCategory.isChecked
            }
        }
    }

    private class ResourceCategoryCell(val resourceCategory: ResourceCategory, var selected: Boolean)
}
