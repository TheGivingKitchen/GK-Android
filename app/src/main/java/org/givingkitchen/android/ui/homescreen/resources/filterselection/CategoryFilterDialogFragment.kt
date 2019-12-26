package org.givingkitchen.android.ui.homescreen.resources.filterselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_category_filter_dialog.*
import org.givingkitchen.android.R

class CategoryFilterDialogFragment: DialogFragment() {
    private val saveButtonClicks: PublishSubject<List<String>> = PublishSubject.create()
    private val categoriesAdapter = ResourceCategoriesAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category_filter_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_resourcesCategoryFilterDialog.apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = categoriesAdapter
        }

        cancelButton_resourcesCategoryFilterDialog.setOnClickListener { dismiss() }
        saveButton_resourcesCategoryFilterDialog.setOnClickListener(saveButtonClickListener)
    }

    fun saveButtonClicks(): Observable<List<String>> = saveButtonClicks

    private val saveButtonClickListener = View.OnClickListener {
        saveButtonClicks.onNext(categoriesAdapter.getSelectedFilters())
        dismiss()
    }
}