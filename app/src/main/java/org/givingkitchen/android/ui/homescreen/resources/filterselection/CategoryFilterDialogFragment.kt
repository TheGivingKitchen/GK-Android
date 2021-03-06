package org.givingkitchen.android.ui.homescreen.resources.filterselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_category_filter_dialog.*
import org.givingkitchen.android.R

class CategoryFilterDialogFragment(currentlySelectedCategories: Set<String>): DialogFragment() {
    private val saveButtonClicks: PublishSubject<List<String>> = PublishSubject.create()
    private val categoriesAdapter = ResourceCategoriesAdapter(currentlySelectedCategories)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_filter_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_resourcesCategoryFilterDialog.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = categoriesAdapter
            this.setHasFixedSize(true)
        }

        categoriesAdapter.updateSaveButtonState().subscribe { updateSaveButtonState(it) }
        cancelButton_resourcesCategoryFilterDialog.setOnClickListener { dismiss() }
        saveButton_resourcesCategoryFilterDialog.setOnClickListener(saveButtonClickListener)
        toggleAllButton_resourcesCategoryFilterDialog.setOnClickListener(toggleButtonClickListener)
    }

    fun saveButtonClicks(): Observable<List<String>> = saveButtonClicks

    private fun updateSaveButtonState(enable: Boolean) {
        if (enable) {
            saveButton_resourcesCategoryFilterDialog.setOnClickListener(saveButtonClickListener)
            saveButton_resourcesCategoryFilterDialog.setTextColor(ContextCompat.getColor(context!!, R.color.gk_blue))
        } else {
            saveButton_resourcesCategoryFilterDialog.setOnClickListener(null)
            saveButton_resourcesCategoryFilterDialog.setTextColor(ContextCompat.getColor(context!!, R.color.gk_blue_a30))
        }
    }

    private val saveButtonClickListener = View.OnClickListener {
        saveButtonClicks.onNext(categoriesAdapter.getSelectedFilters())
        dismiss()
    }

    private val toggleButtonClickListener = View.OnClickListener {
        updateSaveButtonState(categoriesAdapter.toggleAllCheckboxes())
    }
}