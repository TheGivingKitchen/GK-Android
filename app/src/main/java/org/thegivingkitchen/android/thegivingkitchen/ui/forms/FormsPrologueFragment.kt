package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_forms_prologue.*
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formShareWufooUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formsArg
import org.thegivingkitchen.android.thegivingkitchen.util.Firebase
import org.thegivingkitchen.android.thegivingkitchen.util.startShareAction
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FormsPrologueFragment : Fragment() {
    private lateinit var model: FormsPrologueViewModel
    private lateinit var jsonAdapter: JsonAdapter<Form>
    private var shareString: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jsonAdapter = Firebase.moshi.adapter(Form::class.java)
        model = ViewModelProviders.of(this).get(FormsPrologueViewModel::class.java)
        model.getCurrentJson().observe(this, Observer<Form> { liveData ->
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
        // todo: delete this file when done
        val localFile = File.createTempFile("form", "json")
        shareButton_formsPrologue.setOnClickListener(shareClickListener)

        if (arguments != null) {
            Firebase.firebaseInstance.getReferenceFromUrl(arguments!!.getString(formsArg))
                    .getFile(localFile)
                    .addOnSuccessListener {
                        val stringBuilder = StringBuilder()
                        progressBar_formsPrologue.visibility = View.GONE
                        try {
                            val bufferedReader = BufferedReader(FileReader(localFile))
                            var line = bufferedReader.readLine()
                            while (line != null) {
                                stringBuilder.append(line)
                                line = bufferedReader.readLine()
                            }
                            bufferedReader.close()
                            val formData = jsonAdapter.nullSafe().fromJson(stringBuilder.toString())
                            if (formData != null) {
                                model.setCurrentJson(formData)
                             }
                        } catch (e: IOException) {
                            progressBar_safetynetTab.visibility = View.GONE
                            // todo: log error
                        }
                    }.addOnFailureListener {
                        // todo: log error and show error state
                    }
        }
    }

    private fun updateJson(data: Form) {
        title_formsPrologue.text = data.FormTitle
        subtitle_formsPrologue.text = data.FormSubtitle
        description_formsPrologue.text = data.FormMetadata
        shareString = data.FormShareString + " " + formShareWufooUrl + data.ID
        startButton_formsPrologue.visibility = View.VISIBLE
        shareButton_formsPrologue.visibility = View.VISIBLE
    }

    private val shareClickListener = View.OnClickListener {
        if (shareString != null) {
            startShareAction(shareString!!)
        }
    }
}