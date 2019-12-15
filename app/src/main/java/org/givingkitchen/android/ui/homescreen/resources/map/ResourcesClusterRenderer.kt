package org.givingkitchen.android.ui.homescreen.resources.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ResourcesClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<ResourcesMarkerItem>): DefaultClusterRenderer<ResourcesMarkerItem>(context, map, clusterManager) {
    override fun onClusterItemRendered(clusterItem: ResourcesMarkerItem?, marker: Marker?) {
        super.onClusterItemRendered(clusterItem, marker)
        if (clusterItem != null && marker != null) {
            marker.tag = clusterItem.resourceProvider
        }
    }
}