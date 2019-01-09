package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_forms_prologue.*
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formsArg
import org.thegivingkitchen.android.thegivingkitchen.util.Firebase
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FormsPrologueFragment : Fragment() {
    private lateinit var model: FormsPrologueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(FormsPrologueViewModel::class.java)
        model.getCurrentJson().observe(this, Observer<String> { liveData ->
            updateJson(liveData!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forms_prologue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // textView_forms.text = arguments?.getString(formsArg)
        // todo: delete this file when done
        val localFile = File.createTempFile("form", "json")
        if (arguments != null) {
            Firebase.firebaseInstance.getReferenceFromUrl(arguments!!.getString(formsArg))
                    .getFile(localFile)
                    .addOnSuccessListener {
                        val stringBuilder = StringBuilder()
                        progressBar_forms.visibility = View.GONE
                        try {
                            val bufferedReader = BufferedReader(FileReader(localFile))
                            var line = bufferedReader.readLine()
                            while (line != null) {
                                stringBuilder.append(line)
                                line = bufferedReader.readLine()
                            }
                            bufferedReader.close()
                            val jsonString = stringBuilder.toString()
//                              val safetynetData = jsonAdapter.nullSafe().fromJson(jsonString)?.safetyNet
 //                            if (safetynetData != null) {
                                model.setCurrentJson(jsonString)
//                             }
                        } catch (e: IOException) {
                            progressBar_safetynetTab.visibility = View.GONE
                            // todo: log error
                        }
                    }.addOnFailureListener {
                        // todo: log error and show error state
                    }
        }
    }

    private fun updateJson(data: String) {
        textView_forms.text = data
    }
}