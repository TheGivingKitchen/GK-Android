package org.givingkitchen.android.ui.homescreen.resources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.Services
import org.givingkitchen.android.util.Services.moshi
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class ResourcesViewModel : ViewModel() {

    companion object {
        const val safetynetDataUrl =  "${Constants.firebaseStorageUrl}/safetyNet/safetyNet.json"
    }

    private var resourceProviders: MutableLiveData<MutableList<ResourceProvider>> = MutableLiveData()
    private var progressBarVisible: MutableLiveData<Boolean> = MutableLiveData()

    fun getResourceProviders(): LiveData<MutableList<ResourceProvider>> {
        return resourceProviders
    }

    private fun setResourceProviders(data: MutableList<ResourceProvider>) {
        resourceProviders.value = data
    }

    fun isProgressBarVisible(): LiveData<Boolean> {
        return progressBarVisible
    }

    private fun setProgressBarVisibility(visibility: Boolean) {
        progressBarVisible.value = visibility
    }

    fun loadResourceProviders() {
        val localFile = File.createTempFile("safetynet", "json")
        setProgressBarVisibility(true)

        Services.firebaseInstance.getReferenceFromUrl(safetynetDataUrl)
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
                        moshi.adapter(ResourceProvidersList::class.java)
                                .nullSafe()
                                .fromJson(stringBuilder.toString())?.safetyNet?.let {
                            setResourceProviders(it.toMutableList())
                        }
                        setProgressBarVisibility(false)
                    } catch (e: IOException) {
                        setProgressBarVisibility(false)
                        Crashlytics.log("Trouble reading Safetynet json file")
                    }
                }.addOnFailureListener {
                    setProgressBarVisibility(false)
                    Crashlytics.log("Could not download Safetynet data from Firebase")
                }

        localFile.deleteOnExit()
    }
}