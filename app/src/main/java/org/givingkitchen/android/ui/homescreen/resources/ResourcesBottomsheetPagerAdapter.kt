package org.givingkitchen.android.ui.homescreen.resources

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class ResourcesBottomsheetPagerAdapter: PagerAdapter() {
    override fun getCount(): Int {
        return 2
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = when (position) {
            0 -> {
                ResourceProviderIndexView(container.context)
            }
            1 -> {
                ResourceProviderIndexView(container.context)
            }
            else -> {
                ResourceProviderIndexView(container.context)
            }
        }

        container.addView(page)
        return page
    }
}