package org.givingkitchen.android.ui.homescreen.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crashlytics.android.Crashlytics
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_resources.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.safetynet.Header
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvider
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvidersList
import org.givingkitchen.android.util.Services
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class ResourcesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var model: ResourcesViewModel
    private lateinit var bottomsheetPagerAdapter: ResourcesBottomsheetPagerAdapter

    private var serverJson: MutableList<Any>? = null
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(ResourcesViewModel::class.java)
        bottomsheetPagerAdapter = ResourcesBottomsheetPagerAdapter()

        model.getCurrentJson().observe(this, Observer<List<SocialServiceProvider>> { liveData ->
            setServerJson(liveData!!)
        })

        getData()
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
        viewPager_resourcesBottomsheet.adapter = bottomsheetPagerAdapter
        viewPager_resourcesBottomsheet.offscreenPageLimit = bottomsheetPagerAdapter.numberOfPages
    }

    override fun onMapReady(map: GoogleMap) {
        val atlantaLatLang = LatLng(33.774381, -84.372775)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(atlantaLatLang, 10f))
    }

    private fun setServerJson(data: List<SocialServiceProvider>) {
        val dataMutableList = data.toMutableList<Any>()
        serverJson = dataMutableList
        model.setProgressBarVisibility(false)
        setDefaultResults()
    }

    // Show header and serverJson
    private fun setDefaultResults() {
        // This can occur upon recreation due to SearchTextListener
        if (serverJson == null) {
            return
        }

        bottomsheetPagerAdapter.items.clear()
        val dataMutableList = serverJson!!.toMutableList()
        dataMutableList.add(0, Header())
        bottomsheetPagerAdapter.items = dataMutableList
        bottomsheetPagerAdapter.notifyDataSetChanged()
    }

    private fun getData() {
        val localFile = File.createTempFile("safetynet", "json")
        model.setProgressBarVisibility(true)
        val jsonAdapter = Services.moshi.adapter(SocialServiceProvidersList::class.java)

        Services.firebaseInstance.getReferenceFromUrl(ResourcesViewModel.resourcesDataUrl)
                .getFile(localFile)
                .addOnSuccessListener {
                    val stringBuilder = StringBuilder()
                    try {
                        val bufferedReader = BufferedReader(FileReader(localFile))
                        var line = bufferedReader.readLine()
                        while (line != null) {
                            stringBuilder.append(line)
                            line = bufferedReader.readLine()
                        }
                        bufferedReader.close()
                        val jsonString = stringBuilder.toString()
                        val safetynetData = jsonAdapter.nullSafe().fromJson(jsonString)?.safetyNet
                        if (safetynetData != null) {
                            for (i in 0 until safetynetData.size) {
                                safetynetData[i].index = i
                            }
                            model.setCurrentJson(safetynetData)
                        }
                    } catch (e: IOException) {
                        model.setProgressBarVisibility(false)
                        Crashlytics.log("Trouble reading Safetynet json file")
                    }
                }.addOnFailureListener {
                    Crashlytics.log("Could not download Safetynet data from Firebase")
                }

        localFile.deleteOnExit()
    }
}
