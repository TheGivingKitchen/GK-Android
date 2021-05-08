package org.givingkitchen.android.ui.homescreen.assistance

import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_assistance.*
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.util.*
import org.givingkitchen.android.util.Constants.formsArg
import java.util.*

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
        when (resources.currentLocale.language) {
            "es" -> {
                CustomTabs.openCustomTab(context, AssistanceViewModel.assistanceLearnMoreESURL)
            }
            else -> {
                val args = Bundle()
                args.putString(formsArg, AssistanceViewModel.selfAssistanceInquiryUrl)
                args.putEnum(Constants.donePageArg, DonePage.ASSISTANCE_INQUIRY)
                Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
            }
        }
    }

    private val forSomeoneElseButtonClickListener = View.OnClickListener {
        when (resources.currentLocale.language) {
            "es" -> {
                CustomTabs.openCustomTab(context, AssistanceViewModel.assistanceLearnMoreESURL)
            }
            else -> {
                val args = Bundle()
                args.putString(formsArg, AssistanceViewModel.referralAssistanceInquiryUrl)
                args.putEnum(Constants.donePageArg, DonePage.ASSISTANCE_INQUIRY)
                Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
            }
        }
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        Analytics.logLearnedMore("assistance_home")
        CustomTabs.openCustomTab(context, AssistanceViewModel.assistanceLearnMoreURL)
    }
}