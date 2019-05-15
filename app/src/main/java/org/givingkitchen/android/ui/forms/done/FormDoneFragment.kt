package org.givingkitchen.android.ui.forms.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_form_done.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.DonePage
import org.givingkitchen.android.util.getEnum

class FormDoneFragment : Fragment() {

    // todo: Android back button should go back to homepage at this screen
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val donePage: DonePage = try {
            arguments!!.getEnum<DonePage>(Constants.donePageArg)
        } catch (e: KotlinNullPointerException) {
            DonePage.DEFAULT
        }

        header_formDone.setImageDrawable(resources.getDrawable(donePage.drawable, context!!.theme))
        title_formDone.text = resources.getString(donePage.title)
        description_formDone.text = resources.getString(donePage.description)
        button_formDone.text = resources.getString(donePage.buttonText)
        shareButton_formDone.visibility = if (donePage.enableShareButton) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}