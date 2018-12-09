package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet.SafetynetViewModel.Companion.safetynetDataUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Firebase.firebaseInstance
import java.io.*

class SafetynetFragment : Fragment() {
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

        firebaseInstance.getReferenceFromUrl(safetynetDataUrl)
                .getFile(localFile)
                .addOnSuccessListener {
            val stringBuilder = StringBuilder()
                    progressBar_safetynetTab.visibility = View.GONE
            try {
                val bufferedReader = BufferedReader(FileReader(localFile))
                var line = bufferedReader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = bufferedReader.readLine()
                }
                bufferedReader.close()
                textview_safetynet.text = stringBuilder.toString()
            } catch (e: IOException) {
                progressBar_safetynetTab.visibility = View.GONE
                // todo: log error
            }

        }.addOnFailureListener {
            // todo: log error and show error state
        }
    }
}