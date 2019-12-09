package org.givingkitchen.android.ui.homescreen.resources

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
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
import org.givingkitchen.android.util.hasQuery
import org.givingkitchen.android.util.hideKeyboard
import org.givingkitchen.android.util.setNewState

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    companion object {
        private const val atlantaLatitude = 33.774381
        private const val atlantaLongitude = -84.372775
        private const val cityMapZoomLevel = 10f
        private const val detailMapZoomLevel = 16f
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "SafetynetFragment.Tag.ResourceProviderDetailsFragment"
        private const val TAG_RESOURCE_PERMISSIONS_REQUEST_DIALOG = "SafetynetFragment.Tag.LocationPermissionRequestDialogFragment"
        private const val PERMISSIONS_REQUEST_CODE_LOCATION = 0
    }

    private lateinit var model: ResourcesViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

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
        if (map == null) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map_resourcesTab) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }

        recyclerView_resourcesTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_resourcesTab.adapter = adapter

        sheetBehavior = BottomSheetBehavior.from(bottomSheet_resourcesTab)
        sheetBehavior.isFitToContents = false

        searchView_resourcesTab.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                expandBottomSheet(searchView_resourcesTab.hasQuery())
            } else {
                collapseBottomSheet()
                activity.hideKeyboard()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE_LOCATION -> {
                // If request is cancelled, the result arrays is empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    moveMapToUsersLocation()
                }
            }
            else -> {
                Crashlytics.log("Encountered unexpected permission request code: $requestCode")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                val locationPermissionRequestDialog = LocationPermissionRequestDialogFragment()
                locationPermissionRequestDialog.setOnCompleteListener {
                    if (it) {
                        moveMapToUsersLocation()
                    } else {
                        Toast.makeText(context, "permission request dialog returned false", Toast.LENGTH_SHORT).show()
                    }
                }
                locationPermissionRequestDialog.show(fragmentManager, TAG_RESOURCE_PERMISSIONS_REQUEST_DIALOG)
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSIONS_REQUEST_CODE_LOCATION)

                Toast.makeText(context, "No explanation needed, we can request the permission.", Toast.LENGTH_SHORT).show()
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(context, "Permission has already been granted", Toast.LENGTH_SHORT).show()
            moveMapToUsersLocation()
        }

        map!!.setInfoWindowAdapter(ResourcesMapInfoWindowAdapter(context!!))
        showData()
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
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

    private fun moveMapToUsersLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    location?.let {
                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), cityMapZoomLevel))
                    }
                }
                .addOnFailureListener {
                    val x = 7
                }
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
            map!!.setOnMarkerClickListener(markerClusterManager)

            map!!.setOnInfoWindowClickListener {
                // showResourceProviderDetails(it.tag as ResourceProvider)
                // showResourceProviderDetails(it.title)
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
