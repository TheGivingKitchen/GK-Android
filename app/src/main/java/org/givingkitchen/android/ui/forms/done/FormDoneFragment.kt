package org.givingkitchen.android.ui.forms.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_form_done.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.*

class FormDoneFragment : Fragment(), FragmentBackPressedListener {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val donePage: DonePage = arguments!!.getEnum<DonePage>(Constants.donePageArg, DonePage.DEFAULT)

        header_formDone.setImageDrawable(resources.getDrawable(donePage.drawable, context!!.theme))
        title_formDone.text = resources.getString(donePage.title)
        description_formDone.text = resources.getString(donePage.description)
        button_formDone.text = resources.getString(donePage.buttonText)
        shareButton_formDone.visibility = if (donePage.shareString != null) {
            setShareClickListener(resources.getString(donePage.shareString))
            View.VISIBLE
        } else {
            View.GONE
        }
        button_formDone.setOnClickListener(doneButtonClickListener)
    }

    override fun onBackPressed(): Boolean {
        Navigation.findNavController(view!!).navigate(R.id.homeFragment)
        return true
    }

    private fun setShareClickListener(shareUrl: String) {
        shareButton_formDone.setOnClickListener { startShareAction(shareUrl) }
    }

    private val doneButtonClickListener = View.OnClickListener {
        Navigation.findNavController(view!!).navigate(R.id.homeFragment)
    }
}