package org.givingkitchen.android.ui.homescreen.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.safetynet.providerdetails.ResourceProviderDetailsFragment
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetFragment

class ResourcesFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "ResourcesFragment.Tag.ResourcesIndexFragment"
    }

    private lateinit var googleMap: GoogleMap

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // val fragment = ResourcesIndexFragment.newInstance()
        // fragment.isCancelable = false
        // fragment.show(childFragmentManager, TAG_RESOURCE_PROVIDER_BOTTOMSHEET)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map!!

        val atlantaLatLang = LatLng(33.774381, -84.372775)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atlantaLatLang, 10f))
    }
}