package org.givingkitchen.android.ui.homescreen.resources.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ResourcesMarkerItem(val latitude: Double, val longitude: Double, val name: String, val description: String?, val tag: String): ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return description
    }
}