package org.givingkitchen.android.ui.homescreen.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_resources.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.hasQuery
import org.givingkitchen.android.util.hideKeyboard
import org.givingkitchen.android.util.setNewState

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val atlantaLatitude = 33.774381
        private const val atlantaLongitude = -84.372775
        private const val defaultMapZoomLevel = 10f
        private const val detailMapZoomLevel = 16f
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "SafetynetFragment.Tag.ResourceProviderDetailsFragment"
    }

    private lateinit var model: ResourcesViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private var adapter: ResourcesAdapter = ResourcesAdapter(mutableListOf<Any>())
    private var map: GoogleMap? = null
    private var resourceProviders: MutableList<ResourceProvider>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.resourceProviderClicks().subscribe {
            showResourceProviderDetails(it)
            if (it.latitude != null && it.longitude != null) {
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), detailMapZoomLevel))
            }
        }

        model = ViewModelProviders.of(this).get(ResourcesViewModel::class.java)
        model.getResourceProviders().observe(this, Observer<MutableList<ResourceProvider>> { liveData ->
            resourceProviders = liveData
            showData()
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData)
        })
        model.getBottomSheetState().observe(this, Observer<Int> { liveData ->
            updateBottomSheetState(liveData)
        })
        model.loadResourceProviders()
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_resourcesTab) as SupportMapFragment
        mapFragment.getMapAsync(this)

        recyclerView_resourcesTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_resourcesTab.adapter = adapter

        sheetBehavior = BottomSheetBehavior.from(bottomSheet_resourcesTab)
        sheetBehavior.isFitToContents = false

        searchView_resourcesTab.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            if (hasFocus) {
                expandBottomSheet(searchView_resourcesTab.hasQuery())
            } else {
                collapseBottomSheet()
                activity.hideKeyboard()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // map!!.setInfoWindowAdapter(ResourcesMapInfoWindow(context!!))
        showData()
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(atlantaLatitude, atlantaLongitude), defaultMapZoomLevel))
    }

    private fun showResourceProviderDetails(providerData: ResourceProvider) {
        ResourceProviderDetailsFragment
                .newInstance(providerData)
                .show(childFragmentManager, TAG_RESOURCE_PROVIDER_BOTTOMSHEET)
        model.setBottomSheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    }

    private fun expandBottomSheet(showCategoryMenuItems: Boolean) {
        model.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED)
    }

    private fun collapseBottomSheet() {
        model.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED)
    }

    private fun showData() {
        if (resourceProviders != null && map != null) {
            adapter.items = resourceProviders!!
            val markerClusterManager = ClusterManager<ResourcesMarkerItem>(context, map)

            for (resourceProvider in resourceProviders!!) {
                if (resourceProvider.latitude != null && resourceProvider.longitude != null) {
                    val resourcesMarkerItem = ResourcesMarkerItem(resourceProvider.latitude, resourceProvider.longitude, resourceProvider.name, resourceProvider.description)
                    markerClusterManager.addItem(resourcesMarkerItem)
                }
            }

            map!!.setOnCameraIdleListener(markerClusterManager)
            map!!.setOnMarkerClickListener(markerClusterManager.markerManager)

            map!!.setOnInfoWindowClickListener {
                showResourceProviderDetails(it.tag as ResourceProvider)
            }
        }
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_resourcesTab.visibility = View.VISIBLE
                searchView_resourcesTab.visibility = View.GONE
                searchBottomDivider_resourcesTab.visibility = View.GONE
            }
            false -> {
                progressBar_resourcesTab.visibility = View.GONE
                searchView_resourcesTab.visibility = View.VISIBLE
                searchBottomDivider_resourcesTab.visibility = View.VISIBLE
            }
        }
    }

    private fun updateBottomSheetState(state: Int) {
        sheetBehavior.setNewState(state)
    }
}
