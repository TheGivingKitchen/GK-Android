package org.givingkitchen.android.ui.homescreen.resources

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

class ResourcesAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val descriptionExitClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val serviceProviderClicks: PublishSubject<Int> = PublishSubject.create()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_RESOURCE_PROVIDER = 2
    }


}