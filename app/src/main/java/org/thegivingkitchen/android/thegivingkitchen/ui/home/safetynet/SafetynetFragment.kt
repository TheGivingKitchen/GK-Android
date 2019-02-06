package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

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
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet.SafetynetViewModel.Companion.safetynetDataUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Services.firebaseInstance
import org.thegivingkitchen.android.thegivingkitchen.util.Services.moshi
import java.io.*

class SafetynetFragment : Fragment() {
    private lateinit var jsonAdapter: JsonAdapter<SocialServiceProvidersList>
    private lateinit var model: SafetynetViewModel
    private var adapter = SafetynetAdapter(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(SafetynetViewModel::class.java)
        jsonAdapter = moshi.adapter(SocialServiceProvidersList::class.java)
        model.getCurrentJson().observe(this, Observer<List<SocialServiceProvider>> { liveData ->
            updateJson(liveData!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_safetynet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo: delete this file when done
        val localFile = File.createTempFile("safetynet", "json")
        progressBar_safetynetTab.visibility = View.VISIBLE
        recyclerView_safetynetTab.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView_safetynetTab.adapter = adapter

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
                            model.setCurrentJson(safetynetData)
                        }
                    } catch (e: IOException) {
                        // todo: log error
                    }

                }.addOnFailureListener {
                    // todo: log error and show error state
                }
    }

    private fun updateJson(data: List<SocialServiceProvider>) {
        adapter.items = data
        adapter.notifyDataSetChanged()
        progressBar_safetynetTab.visibility = View.GONE
    }
}