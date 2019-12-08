package org.givingkitchen.android.ui.homescreen.resources

import android.content.Context
import android.view.View
import android.widget.EditText
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class ResourcesMapInfoWindow(val context: Context): GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(p0: Marker?): View {
        return EditText(context)
    }

    override fun getInfoContents(p0: Marker?): View {
        return EditText(context)
    }
}