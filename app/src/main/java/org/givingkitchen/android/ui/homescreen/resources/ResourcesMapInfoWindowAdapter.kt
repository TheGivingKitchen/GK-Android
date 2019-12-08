package org.givingkitchen.android.ui.homescreen.resources

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setPaddingDp

class ResourcesMapInfoWindowAdapter(val context: Context): GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View {
        return ResourcesMapInfoWindow(context)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}

class ResourcesMapInfoWindow(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): ConstraintLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_resources_infowindow, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.setBackgroundResource(R.drawable.resources_infowindow)
    }
}