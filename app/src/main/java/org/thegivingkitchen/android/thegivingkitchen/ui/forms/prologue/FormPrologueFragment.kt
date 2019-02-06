package org.thegivingkitchen.android.thegivingkitchen.ui.forms.prologue

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_form_prologue.*
import kotlinx.android.synthetic.main.fragment_safetynet.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Form
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Page
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formShareWufooUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formsArg
import org.thegivingkitchen.android.thegivingkitchen.util.Services
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import org.thegivingkitchen.android.thegivingkitchen.util.startShareAction
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FormPrologueFragment : Fragment() {
    private lateinit var model: FormPrologueViewModel
    private lateinit var jsonAdapter: JsonAdapter<Form>
    private var shareString: String? = ""
    private var questionPages = listOf<Page>()

    companion object {
        const val questionPagesArg = "questionPages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // todo: set cancel button action
        super.onCreate(savedInstanceState)
        jsonAdapter = Services.moshi.adapter(Form::class.java)
        model = ViewModelProviders.of(this).get(FormPrologueViewModel::class.java)
        model.getCurrentJson().observe(this, Observer<Form> { liveData ->
            updateJson(liveData!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_prologue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo: delete this file when done
        val localFile = File.createTempFile("form", "json")
        shareButton_formsPrologue.setOnClickListener(shareClickListener)
        cancel_formsPrologue.setOnClickListener(cancelClickListener)
        if (arguments != null) {
            Services.firebaseInstance.getReferenceFromUrl(arguments!!.getString(formsArg)!!)
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
        startButton_formsPrologue.setOnClickListener(startButtonClickListener)
    }

    private fun updateJson(data: Form) {
        title_formsPrologue.setTextIfItExists(data.FormTitle)
        subtitle_formsPrologue.setTextIfItExists(data.FormSubtitle)
        description_formsPrologue.setTextIfItExists(data.FormMetadata)
        shareString = data.FormShareString + " " + formShareWufooUrl + data.ID
        if (data.Pages != null) {
            questionPages = data.Pages
        }
        startButton_formsPrologue.visibility = View.VISIBLE
        shareButton_formsPrologue.visibility = View.VISIBLE
    }

    private val startButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putParcelableArrayList(questionPagesArg, ArrayList(questionPages))
        Navigation.findNavController(view!!).navigate(R.id.questionsContainerFragment, args)
    }

    private val shareClickListener = View.OnClickListener {
        if (shareString != null) {
            startShareAction(shareString!!)
        }
    }

    private val cancelClickListener = View.OnClickListener {
        Toast.makeText(context, "cancel button clicked", Toast.LENGTH_SHORT).show()
    }
}