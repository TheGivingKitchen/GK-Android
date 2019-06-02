package org.givingkitchen.android.ui.forms.prologue

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.crashlytics.android.Crashlytics
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.fragment_form_prologue.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Form
import org.givingkitchen.android.ui.forms.Page
import org.givingkitchen.android.util.*
import org.givingkitchen.android.util.Constants.donePageArg
import org.givingkitchen.android.util.Constants.formShareWufooUrl
import org.givingkitchen.android.util.Constants.formsArg
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FormPrologueFragment : Fragment() {
    private lateinit var model: FormPrologueViewModel
    private lateinit var jsonAdapter: JsonAdapter<Form>
    private var shareString: String? = ""
    private var form: Form? = null

    companion object {
        const val formArg = "formPages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jsonAdapter = Services.moshi.adapter(Form::class.java)
        model = ViewModelProviders.of(this).get(FormPrologueViewModel::class.java)
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_prologue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getCurrentJson().observe(this, Observer<Form> { liveData ->
            updateJson(liveData!!)
        })
        model.isProgressBarVisible().observe(this, Observer<Boolean> { liveData ->
            updateProgressBarVisibility(liveData!!)
        })
        getData()

        shareButton_formsPrologue.setOnClickListener(shareClickListener)
        cancel_formsPrologue.setOnClickListener(cancelClickListener)
        startButton_formsPrologue.setOnClickListener(startButtonClickListener)
    }

    private fun getData() {
        if (arguments != null) {
            val localFile = File.createTempFile("form", "json")
            model.setProgressBarVisibility(true)

            val formUrl = arguments!!.getString(formsArg)!!
            Services.firebaseInstance.getReferenceFromUrl(formUrl)
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
                            val formData = jsonAdapter.nullSafe().fromJson(stringBuilder.toString())
                            if (formData != null) {
                                model.setCurrentJson(formData)
                            }
                        } catch (e: IOException) {
                            model.setProgressBarVisibility(false)
                            Crashlytics.log("Trouble reading data file for form $formUrl")
                        }
                    }.addOnFailureListener {
                        Crashlytics.log("Could not get data file for form $formUrl")
                    }

            localFile.deleteOnExit()
        } else {
            Crashlytics.log("Arguments passed to form prologue were null")
        }
    }

    private fun updateJson(data: Form) {
        title_formsPrologue.setTextIfItExists(data.FormTitle)
        subtitle_formsPrologue.setTextIfItExists(data.FormSubtitle)
        description_formsPrologue.setTextIfItExists(data.FormMetadata)
        shareString = data.FormShareString + " " + formShareWufooUrl + data.ID
        form = data
        startButton_formsPrologue.visibility = View.VISIBLE
        shareButton_formsPrologue.visibility = View.VISIBLE
        model.setProgressBarVisibility(false)
    }

    private fun updateProgressBarVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                progressBar_formsPrologue.visibility = View.VISIBLE
            }
            false -> {
                progressBar_formsPrologue.visibility = View.GONE
            }
        }
    }

    private val startButtonClickListener = View.OnClickListener {
        if (form != null && arguments != null) {
            val args = Bundle()
            args.putParcelable(formArg, form)
            val donePage: DonePage = arguments!!.getEnum<DonePage>(donePageArg, DonePage.DEFAULT)

            args.putEnum(donePageArg, donePage)
            Navigation.findNavController(view!!).navigate(R.id.questionsContainerFragment, args)
        } else {
            Toast.makeText(context, getString(R.string.base_forms_prologue_error), Toast.LENGTH_SHORT).show()
            Crashlytics.log("Could not enter form")
        }
    }

    private val shareClickListener = View.OnClickListener {
        if (shareString != null) {
            startShareAction(shareString!!)
        }
    }

    private val cancelClickListener = View.OnClickListener {
        findNavController().navigateUp()
    }
}