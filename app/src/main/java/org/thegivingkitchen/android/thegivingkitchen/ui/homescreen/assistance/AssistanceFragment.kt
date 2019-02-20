package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import org.thegivingkitchen.android.thegivingkitchen.R
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_assistance.*
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance.AssistanceViewModel.Companion.assistanceLearnMoreURL
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance.AssistanceViewModel.Companion.referralAssistanceInquiryUrl
import org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.assistance.AssistanceViewModel.Companion.selfAssistanceInquiryUrl
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.formsArg
import org.thegivingkitchen.android.thegivingkitchen.util.CustomTabs

class AssistanceFragment : Fragment() {
    private lateinit var model: AssistanceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(AssistanceViewModel::class.java)
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_assistance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header_description_assistanceTab.setText(model.setHeaderText(context!!), TextView.BufferType.SPANNABLE)
        for_you_button_assistanceTab.setOnClickListener(forYouButtonClickListener)
        for_someone_else_button_assistanceTab.setOnClickListener(forSomeoneElseButtonClickListener)
        learn_more_button_assistanceTab.setOnClickListener(learnMoreButtonClickListener)
    }

    private val forYouButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(formsArg, selfAssistanceInquiryUrl)
        Navigation.findNavController(getView()!!).navigate(R.id.formsFragment, args)
    }

    private val forSomeoneElseButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(formsArg, referralAssistanceInquiryUrl)
        Navigation.findNavController(getView()!!).navigate(R.id.formsFragment, args)
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, assistanceLearnMoreURL)
    }
}