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

/**
 *
 *  val providerData = SocialServiceProvider(index=3, name="Ben Massell Dental Clinic", address="700 14th Street NW, Atlanta, GA 30308", website="https://www.benmasselldentalclinic.org", phone="404-881-1858", contactName="Keith Kirshner", category="Dental", description="Needs-based dental services.", countiesServed="Butts, Cherokee, Clayton, Cobb, Coweta, DeKalb, Douglas, Fayette, Fulton, Gwinnett, Henry, Paulding, Rockdale", latitude = 0.0, longitude = 0.0)

 */

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    companion object {
        const val atlantaLatitude = 33.774381
        const val atlantaLongitude = -84.372775
        const val defaultMapZoomLevel = 10f
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) =
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(atlantaLatitude, atlantaLongitude), defaultMapZoomLevel))

}
