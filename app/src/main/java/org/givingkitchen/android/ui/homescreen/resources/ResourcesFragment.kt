package org.givingkitchen.android.ui.homescreen.resources

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_resources.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.resources.bottomsheet.ResourceProviderDetailsFragment
import org.givingkitchen.android.ui.homescreen.resources.bottomsheet.ResourcesAdapter
import org.givingkitchen.android.ui.homescreen.resources.filterselection.CategoryFilterDialogFragment
import org.givingkitchen.android.ui.homescreen.resources.map.ResourcesClusterRenderer
import org.givingkitchen.android.ui.homescreen.resources.map.ResourcesMapInfoWindowAdapter
import org.givingkitchen.android.ui.homescreen.resources.map.ResourcesMarkerItem
import org.givingkitchen.android.util.getGivingKitchenSharedPreferences
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val cityMapZoomLevel = 10f
        private const val detailMapZoomLevel = 16f
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "ResourcesFragment.Tag.ResourceProviderDetailsFragment"
        private const val TAG_INTRO_DIALOG = "ResourcesFragment.Tag.IntroDialogFragment"
        private const val TAG_FILTER_DIALOG = "ResourcesFragment.Tag.CategoryFilterDialogFragment"
        private const val PERMISSIONS_REQUEST_CODE_LOCATION = 0
        private val atlanta = LatLng(33.774381, -84.372775)
    }

    private lateinit var model: ResourcesViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var markerClusterManager: ClusterManager<ResourcesMarkerItem>
    private lateinit var adapter: ResourcesAdapter
    private var map: GoogleMap? = null
    private var resourceProviders: List<ResourceProvider>? = null
    private var bottomsheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    @SuppressWarnings("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ResourcesAdapter()
        adapter.resourceProviderClicks().subscribe {
            showResourceProviderDetails(it)
            updateBottomsheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
            clearSearchViewFocus()
            if (it.latitude != null && it.longitude != null) {
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), detailMapZoomLevel))
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        model = ViewModelProviders.of(this).get(ResourcesViewModel::class.java)
        model.getResourceProviders().observe(this, Observer<List<ResourceProvider>> { liveData ->
            resourceProviders = liveData
            showData()
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData)
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
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        searchView_resourcesTab.setOnQueryTextListener(SearchTextListener())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (map == null) {
            val showIntroDialog = activity.getGivingKitchenSharedPreferences()?.getBoolean(getString(R.string.resources_intro_dialog_key), true) ?: false
            if (showIntroDialog) {
                val resourcesIntroDialogFragment = ResourcesIntroDialogFragment()
                resourcesIntroDialogFragment.dialogDismissed().subscribe {
                    showMap()
                    activity.getGivingKitchenSharedPreferences()?.let {
                        with(it.edit()) {
                            putBoolean(getString(R.string.resources_intro_dialog_key), false)
                            apply()
                        }
                    }
                }
                resourcesIntroDialogFragment.show(fragmentManager!!, TAG_INTRO_DIALOG)
            } else {
                showMap()
            }
        }

        sheetBehavior = BottomSheetBehavior.from(bottomSheet_resourcesTab)
        sheetBehavior.isFitToContents = false
        updateBottomsheetState(bottomsheetState)
        sheetBehavior.setBottomSheetCallback(bottomSheetBehaviorCallback)

        recyclerView_resourcesTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_resourcesTab.adapter = adapter
        recyclerView_resourcesTab.addOnScrollListener(recyclerViewScrollListener)
        filterButton_resourcesTab.text = getString(R.string.resources_tab_selected_filter_button, getFilterButtonLabelValue(adapter.currentCategoryFilters))

        searchView_resourcesTab.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateBottomsheetState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }

        filterButton_resourcesTab.setOnClickListener(filterButtonClickListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        markerClusterManager = ClusterManager(context, map)
        markerClusterManager.renderer = ResourcesClusterRenderer(context!!, map!!, markerClusterManager)
        map!!.setOnCameraIdleListener(markerClusterManager)
        map!!.setOnMarkerClickListener(markerClusterManager)

        map!!.uiSettings.apply {
            this.isZoomControlsEnabled = true
            this.isMyLocationButtonEnabled = true
            this.isMapToolbarEnabled = true
        }

        requestLocationPermissionIfNeeded()
        map!!.setInfoWindowAdapter(ResourcesMapInfoWindowAdapter(context!!))

        markerClusterManager.markerCollection.setOnInfoWindowAdapter(ResourcesMapInfoWindowAdapter(context!!))
        showData()
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE_LOCATION)
    fun requestLocationPermissionIfNeeded() {
        val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
        if (EasyPermissions.hasPermissions(context!!, locationPermission)) {
            moveMapToUsersLocation()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.resources_tab_location_dialog_message), PERMISSIONS_REQUEST_CODE_LOCATION, locationPermission)
            moveMapToDefaultLocation()
        }
    }

    private fun showResourceProviderDetails(providerData: ResourceProvider) {
        ResourceProviderDetailsFragment
                .newInstance(providerData)
                .show(childFragmentManager, TAG_RESOURCE_PROVIDER_BOTTOMSHEET)
    }

    private fun moveMapToDefaultLocation() {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(atlanta.latitude, atlanta.longitude), cityMapZoomLevel))
    }

    private fun moveMapToUsersLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), cityMapZoomLevel))
                    } ?: run {
                        moveMapToDefaultLocation()
                    }
                }
                .addOnFailureListener {
                    moveMapToDefaultLocation()
                }
    }

    private fun showData() {
        if (resourceProviders != null && map != null) {
            addMarkersToMap(adapter.filterToCategories(null, "", resourceProviders!!))
            map!!.setOnInfoWindowClickListener {
                showResourceProviderDetails(it.tag as ResourceProvider)
            }
        }
    }

    private val filterButtonClickListener = View.OnClickListener {
        val categoryFilterDialogFragment = CategoryFilterDialogFragment(adapter.currentCategoryFilters)
        categoryFilterDialogFragment.saveButtonClicks().subscribe {
            if (it.size == ResourceCategory.resourceCategories.size) {
                filterButton_resourcesTab.text = getString(R.string.resources_tab_selected_filter_button, getString(R.string.resources_tab_filter_all))
                addMarkersToMap(adapter.filterToCategories(null, searchView_resourcesTab.query.toString(), resourceProviders!!))
            } else {
                filterButton_resourcesTab.text = getString(R.string.resources_tab_selected_filter_button, it.joinToString())
                addMarkersToMap(adapter.filterToCategories(it, searchView_resourcesTab.query.toString(), resourceProviders!!))
            }
        }
        categoryFilterDialogFragment.show(fragmentManager!!, TAG_FILTER_DIALOG)
    }

    private fun getFilterButtonLabelValue(selectedCategories: Collection<String>): String {
        return if (selectedCategories.size == ResourceCategory.resourceCategories.size) {
            getString(R.string.resources_tab_filter_all)
        } else {
            selectedCategories.joinToString()
        }
    }

    private fun clearSearchViewFocus() {
        searchView_resourcesTab.clearFocus()
    }

    /**
     * @param state should be one of the BottomSheet states
     * STATE_DRAGGING = 1
     * STATE_SETTLING = 2
     * STATE_EXPANDED = 3
     * STATE_COLLAPSED = 4
     * STATE_HIDDEN = 5
     * STATE_HALF_EXPANDED = 6
     */
    private fun updateBottomsheetState(state: Int) {
        sheetBehavior.state = state
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_resourcesTab.visibility = View.VISIBLE
                resourcesListHeader_resourcesTab.visibility = View.GONE
            }
            false -> {
                progressBar_resourcesTab.visibility = View.GONE
                resourcesListHeader_resourcesTab.visibility = View.VISIBLE
            }
        }
    }

    private fun showMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_resourcesTab) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun addMarkersToMap(resourceProviders: List<ResourceProvider>) {
        markerClusterManager.clearItems()
        for (resourceProvider in resourceProviders) {
            if (resourceProvider.latitude != null && resourceProvider.longitude != null) {
                markerClusterManager.addItem(ResourcesMarkerItem(resourceProvider))
            }
        }
        markerClusterManager.cluster()
    }

    private val recyclerViewScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        clearSearchViewFocus()
                    }
                }
            }

    private val bottomSheetBehaviorCallback =
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    bottomsheetState = newState
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        clearSearchViewFocus()
                    }
                }
            }

    private inner class SearchTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(text: String?): Boolean {
            text?.let {
                addMarkersToMap(adapter.searchWithinCategories(it))
            }
            return true
        }

        override fun onQueryTextSubmit(searchText: String?): Boolean {
            if (!searchText.isNullOrEmpty()) {
                updateBottomsheetState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                clearSearchViewFocus()
            }
            return true
        }
    }
}
