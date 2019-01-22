package org.thegivingkitchen.android.thegivingkitchen.util

import android.support.v7.widget.RecyclerView

abstract class RecyclerViewAdapterWithHeaders<T: RecyclerView.ViewHolder>(var items: List<*>): RecyclerView.Adapter<T>() {
    override fun getItemCount() = items.size
}