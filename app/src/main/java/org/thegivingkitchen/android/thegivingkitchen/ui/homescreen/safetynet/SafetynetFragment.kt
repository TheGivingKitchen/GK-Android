package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.SafetynetViewModel.Companion.safetynetDataUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.givingKitchenUrl
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs
import org.thegivingkitchen.android.thegivingkitchen.util.Services.firebaseInstance
import org.thegivingkitchen.android.thegivingkitchen.util.Services.moshi
import java.io.*

class SafetynetFragment : Fragment() {
    private lateinit var jsonAdapter: JsonAdapter<SocialServiceProvidersList>
    private lateinit var model: SafetynetViewModel
    private var adapter = SafetynetAdapter(mutableListOf())

    companion object {
        private const val learnMoreURL = "$givingKitchenUrl/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(SafetynetViewModel::class.java)
        jsonAdapter = moshi.adapter(SocialServiceProvidersList::class.java)
        model.getCurrentJson().observe(this, Observer<List<SocialServiceProvider>> { liveData ->
            updateJson(liveData!!)
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData!!)
        })
        adapter.learnMoreClicks().subscribe { openLearnMoreLink() }
        adapter.joinUsClicks().subscribe { goToFacebookGroupsScreen() }
        adapter.resourcesFilterClicks().subscribe { showResourcesFilter() }
        adapter.countiesFilterClicks().subscribe { showCountiesFilter() }
        adapter.serviceProviderClicks().subscribe { showProviderData(it) }
        getData()
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_safetynet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_safetynetTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_safetynetTab.adapter = adapter
    }

    private fun getData() {
        // todo: delete this file when done
        val localFile = File.createTempFile("safetynet", "json")
        model.setProgressBarVisibility(true)

        firebaseInstance.getReferenceFromUrl(safetynetDataUrl)
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
                        // todo: log error
                    }

                }.addOnFailureListener {
                    // todo: log error and show error state
                }
    }

    private fun updateJson(data: List<SocialServiceProvider>) {
        val dataMutableList = data.toMutableList<Any>()
        dataMutableList.add(0, Header())
        adapter.items = dataMutableList
        adapter.notifyDataSetChanged()
        model.setProgressBarVisibility(false)
    }

    private fun openLearnMoreLink() {
        CustomTabs.openCustomTab(context, learnMoreURL)
    }

    private fun goToFacebookGroupsScreen() {
        Toast.makeText(context, "Join us on FB clicked", Toast.LENGTH_SHORT).show()
    }

    private fun showProviderData(index: Int) {
        val providerData = model.getCurrentJson().value
        if (providerData != null) {
            Toast.makeText(context, providerData[index].toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showResourcesFilter() {
        Toast.makeText(context, "Resources Filter clicked", Toast.LENGTH_SHORT).show()
    }

    private fun showCountiesFilter() {
        Toast.makeText(context, "Counties Filter clicked", Toast.LENGTH_SHORT).show()
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_safetynetTab.visibility = View.VISIBLE
            }
            false -> {
                progressBar_safetynetTab.visibility = View.GONE
            }
        }
    }
}