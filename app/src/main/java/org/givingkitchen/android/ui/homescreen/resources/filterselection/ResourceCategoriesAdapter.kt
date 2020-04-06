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

class ResourceCategoriesAdapter(private val currentlySelectedCategories: Set<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = ResourceCategory.resourceCategories.map { ResourceCategoryCell(it, it.title in currentlySelectedCategories) }
    private val recyclerViews = createItemViews()
    private val updateSaveButtonState: PublishSubject<Boolean> = PublishSubject.create()

    companion object {
        const val VIEW_TYPE_TITLE = 0
        const val VIEW_TYPE_CATEGORY = 1
    }

    override fun getItemViewType(position: Int) = if (recyclerViews[position] is ResourceCategoryCell) {
        VIEW_TYPE_CATEGORY
    } else {
        VIEW_TYPE_TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TITLE) {
            ResourceCategoryTitleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_resource_category_title, parent, false))
        } else {
            ResourceCategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_resource_category, parent, false), updateSaveButtonState)
        }
    }

    override fun getItemCount() = recyclerViews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ResourceCategoryViewHolder) {
            holder.bind(position)
        }
    }

    fun getSelectedFilters(): List<String> {
        return items.filter { it.selected }.map { it.resourceCategory.title }
    }

    fun updateSaveButtonState(): Observable<Boolean> = updateSaveButtonState

    fun toggleAllCheckboxes(): Boolean {
        var someUnselected = false
        for (item in items) {
            if (!item.selected) {
                someUnselected = true
                item.selected = true
            }
        }

        if (!someUnselected) {
            for (item in items) {
                item.selected = false
            }
        }

        notifyDataSetChanged()
        return !someUnselected
    }

    inner class ResourceCategoryTitleViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView)

    inner class ResourceCategoryViewHolder(override val containerView: View, private val updateSaveButtonState: PublishSubject<Boolean>) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(position: Int) {
            val position = position - 1
            title_resourceCategory.text = items[position].resourceCategory.title
            icon_resourceCategory.setImageDrawable(containerView.resources.getDrawable(items[position].resourceCategory.icon, containerView.context.theme))
            checkbox_resourceCategory.isChecked = items[position].selected

            containerView.setOnClickListener {
                checkbox_resourceCategory.toggle()
            }

            checkbox_resourceCategory.setOnCheckedChangeListener { _, isChecked ->
                items[position].selected = isChecked

                if (isChecked) {
                    updateSaveButtonState.onNext(true)
                } else {
                    var atLeastOneChecked = false
                    for (item in items) {
                        if (item.selected) {
                            updateSaveButtonState.onNext(true)
                            atLeastOneChecked = true
                            continue
                        }
                    }

                    if (!atLeastOneChecked) {
                        updateSaveButtonState.onNext(false)
                    }
                }
            }
        }
    }

    private fun createItemViews(): List<Any> = mutableListOf<Any>().apply {
        this.add(ResourceCategoryTitle())
        this.addAll(items)
    }

    private class ResourceCategoryCell(val resourceCategory: ResourceCategory, var selected: Boolean)
    private class ResourceCategoryTitle
}
