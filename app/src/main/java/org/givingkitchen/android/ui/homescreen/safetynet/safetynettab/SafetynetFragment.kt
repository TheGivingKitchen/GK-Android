package org.givingkitchen.android.ui.homescreen.safetynet.safetynettab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.safetynet.Header
import org.givingkitchen.android.ui.homescreen.safetynet.ResourceProviderDetailsFragment
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvider
import org.givingkitchen.android.ui.homescreen.safetynet.SocialServiceProvidersList
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetViewModel.Companion.safetynetDataUrl
import org.givingkitchen.android.ui.homescreen.safetynet.safetynettab.SafetynetViewModel.Companion.safetynetLearnMoreURL
import org.givingkitchen.android.util.CustomTabs
import org.givingkitchen.android.util.Services.firebaseInstance
import org.givingkitchen.android.util.Services.moshi
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class SafetynetFragment : Fragment() {
    private lateinit var jsonAdapter: JsonAdapter<SocialServiceProvidersList>
    private lateinit var model: SafetynetViewModel
    private lateinit var adapter: SafetynetAdapter
    private var serverJson: MutableList<Any>? = null
    private var searchText: String? = null

    companion object {
        private const val TAG_RESOURCE_PROVIDER_BOTTOMSHEET = "AudienceOverviewFragment.Tag.AudienceListFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(SafetynetViewModel::class.java)
        jsonAdapter = moshi.adapter(SocialServiceProvidersList::class.java)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                ?: return
        val facebookSectionExpanded = sharedPref.getBoolean(getString(R.string.facebook_groups_expanded_key), true)

        adapter = SafetynetAdapter(mutableListOf(), facebookSectionExpanded)
        model.getCurrentJson().observe(this, Observer<List<SocialServiceProvider>> { liveData ->
            setServerJson(liveData!!)
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData!!)
        })
        adapter.learnMoreClicks().subscribe { openLearnMoreLink() }
        adapter.joinUsClicks().subscribe { goToFacebookGroupsScreen() }
        adapter.resourcesFilterClicks().subscribe { showResourcesFilter() }
        adapter.countiesFilterClicks().subscribe { showCountiesFilter() }
        adapter.expandFacebookSectionClicks().subscribe { saveFacebookSectionState(it) }
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
        searchText = searchView_safetynetTab.query.toString()
        searchView_safetynetTab.setOnQueryTextListener(SearchTextListener())
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

    private fun setServerJson(data: List<SocialServiceProvider>) {
        val dataMutableList = data.toMutableList<Any>()
        serverJson = dataMutableList
        model.setProgressBarVisibility(false)
        setDefaultResults()
    }

    // Filters results based on searchText. Hides header if searchText is present.
    private fun setAdapterResults() {
        setDefaultResults()
        if (!searchText.isNullOrEmpty()) {
            adapter.items = adapter.items.filter {
                it is SocialServiceProvider
                        && (searchFields(it.description)
                        || searchFields(it.address)
                        || searchFields(it.category)
                        || searchFields(it.contactName)
                        || searchFields(it.countiesServed)
                        || searchFields(it.name)
                        || searchFields(it.phone)
                        || searchFields(it.website))
            }.toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    private fun searchFields(responseString: String?): Boolean {
        val immutableSearchText = searchText
        return !immutableSearchText.isNullOrBlank()
                && responseString != null
                && responseString.toUpperCase().contains(immutableSearchText.toUpperCase())
    }

    // Show header and serverJson
    private fun setDefaultResults() {
        // This can occur upon recreation due to SearchTextListener
        if (serverJson == null) {
            return
        }

        adapter.items.clear()
        val dataMutableList = serverJson!!.toMutableList()
        dataMutableList.add(0, Header())
        adapter.items = dataMutableList
        adapter.notifyDataSetChanged()
    }

    private fun openLearnMoreLink() {
        CustomTabs.openCustomTab(context, safetynetLearnMoreURL)
    }

    private fun goToFacebookGroupsScreen() {
        Navigation.findNavController(view!!).navigate(R.id.facebookGroupsFragment)
    }

    private fun showProviderData(index: Int) {
        val providerData = model.getCurrentJson().value!![index]
        val fragment = ResourceProviderDetailsFragment.newInstance(providerData)
        fragment.show(childFragmentManager, TAG_RESOURCE_PROVIDER_BOTTOMSHEET)
    }

    private fun saveFacebookSectionState(expanded: Boolean) {
        // todo: troubleshoot this
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.facebook_groups_expanded_key), expanded)
            apply()
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
                searchView_safetynetTab.visibility = View.GONE
            }
            false -> {
                progressBar_safetynetTab.visibility = View.GONE
                searchView_safetynetTab.visibility = View.VISIBLE
            }
        }
    }

    private inner class SearchTextListener : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(text: String?): Boolean {
            searchText = text
            setAdapterResults()
            return true
        }

        override fun onQueryTextSubmit(text: String?): Boolean {
            return true
        }
    }
}