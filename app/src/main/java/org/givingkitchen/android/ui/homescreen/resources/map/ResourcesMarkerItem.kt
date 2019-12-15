package org.givingkitchen.android.ui.homescreen.resources.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import org.givingkitchen.android.ui.homescreen.resources.ResourceProvider

class ResourcesMarkerItem(val resourceProvider: ResourceProvider): ClusterItem {
    override fun getPosition(): LatLng {
        return LatLng(resourceProvider.latitude!!, resourceProvider.longitude!!)
    }

    override fun getTitle(): String? {
        return resourceProvider.name
    }

    override fun getSnippet(): String? {
        return resourceProvider.description
    }
}