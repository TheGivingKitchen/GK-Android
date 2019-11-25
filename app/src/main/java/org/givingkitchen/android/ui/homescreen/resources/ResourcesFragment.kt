package org.givingkitchen.android.ui.homescreen.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_resources.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.safetynet.providerdetails.ResourceProviderDetailsFragment


/**
 *  val providerData = SocialServiceProvider(index=3, name="Ben Massell Dental Clinic", address="700 14th Street NW, Atlanta, GA 30308", website="https://www.benmasselldentalclinic.org", phone="404-881-1858", contactName="Keith Kirshner", category="Dental", description="Needs-based dental services.", countiesServed="Butts, Cherokee, Clayton, Cobb, Coweta, DeKalb, Douglas, Fayette, Fulton, Gwinnett, Henry, Paulding, Rockdale", latitude = 0.0, longitude = 0.0)
 */

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    companion object {
        const val atlantaLatitude = 33.774381
        const val atlantaLongitude = -84.372775
        const val defaultMapZoomLevel = 10f
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "SafetynetFragment.Tag.ResourceProviderDetailsFragment"
    }

    private lateinit var model: ResourcesViewModel
    private lateinit var adapter: ResourcesAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ResourcesAdapter(mutableListOf<Any>())
        adapter.resourceProviderClicks().subscribe { showResourceProviderDetails(it) }

        model = ViewModelProviders.of(this).get(ResourcesViewModel::class.java)
        model.getResourceProviders().observe(this, Observer<MutableList<ResourceProvider>> { liveData ->
            adapter.items = liveData
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_resourcesTab) as SupportMapFragment
        mapFragment.getMapAsync(this)

        recyclerView_resourcesTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_resourcesTab.adapter = adapter

        sheetBehavior = BottomSheetBehavior.from(bottomSheet_resourcesTab);
    }

    override fun onMapReady(map: GoogleMap) =
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(atlantaLatitude, atlantaLongitude), defaultMapZoomLevel))

    private fun showResourceProviderDetails(providerData: ResourceProvider) {
        val fragment = ResourceProviderDetailsFragment.newInstance(providerData)
        fragment.show(childFragmentManager, TAG_RESOURCE_PROVIDER_BOTTOMSHEET)
        if (sheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_resourcesTab.visibility = View.VISIBLE
            }
            false -> {
                progressBar_resourcesTab.visibility = View.GONE
            }
        }
    }
}
