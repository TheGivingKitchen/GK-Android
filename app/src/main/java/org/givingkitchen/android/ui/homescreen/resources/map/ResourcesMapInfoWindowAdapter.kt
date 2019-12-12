package org.givingkitchen.android.ui.homescreen.resources.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.view_resources_infowindow.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setPaddingDp
import org.givingkitchen.android.util.setTextIfItExists

class ResourcesMapInfoWindowAdapter(val context: Context): GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        // return ResourcesMapInfoWindow(context)
        // view.findViewById<TextView>(R.id.learnMoreButton_eventsTab).setOnClickListener { learnMoreClicks.onNext(false) }
        return ResourcesMapInfoWindow(context, marker.title, marker.snippet)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}

class ResourcesMapInfoWindow(context: Context, title: String, description: String, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_resources_infowindow, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setBackgroundResource(R.drawable.resources_infowindow)
        orientation = HORIZONTAL
        title_resourcesInfowindow.text = title
        description_resourcesInfowindow.setTextIfItExists(description)
        setPaddingDp(10, 10, 10, 10)
    }
}