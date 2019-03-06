package org.givingkitchen.android.ui.homescreen.give

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_give.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.creditCardDonationURL
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.giveLearnMoreURL
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.recurringDonationURL
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.CustomTabs
import org.givingkitchen.android.util.getFloatDimension

class GiveFragment : Fragment() {
    private lateinit var model: GiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(GiveViewModel::class.java)
        model.getCurrentDonationData().observe(this, Observer<GiveViewModel.DonationExampleLiveData> { liveData ->
            updateDonationExamples(liveData!!)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_give, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learn_more_button_giveTab.setOnClickListener(learnMoreButtonClickListener)
        card_donate_button_giveTab.setOnClickListener(creditCardDonationClickListener)
        recurring_donate_button_giveTab.setOnClickListener(recurringDonationClickListener)
        leftChevronClickHolder_giveTab.setOnClickListener(leftExamplesArrowClickListener)
        rightChevronClickHolder_giveTab.setOnClickListener(rightExamplesArrowClickListener)
        volunteer_button_giveTab.setOnClickListener(volunteerButtonClickListener)
        partner_button_giveTab.setOnClickListener(partnerButtonClickListener)
        joinOurForcesButton_giveTab.setOnClickListener(joinForcesButtonClickListener)
        // todo: this introduces a bug where the field will be blank if the user
        // is on the last item, and then navigates out and comes back into this screen
        model.onRightArrowClicked()
    }

    private val creditCardDonationClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, creditCardDonationURL)
    }

    private val recurringDonationClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, recurringDonationURL)
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, giveLearnMoreURL)
    }

    private val leftExamplesArrowClickListener = View.OnClickListener {
        model.onLeftArrowClicked()
    }

    private val rightExamplesArrowClickListener = View.OnClickListener {
        model.onRightArrowClicked()
    }

    private val volunteerButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.volunteerSignupUrl)
        Navigation.findNavController(getView()!!).navigate(R.id.formsFragment, args)
    }

    private val joinForcesButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.joinOurForcesUrl)
        Navigation.findNavController(getView()!!).navigate(R.id.formsFragment, args)
    }

    private val partnerButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.stabilityNetworkPartnerUrl)
        Navigation.findNavController(getView()!!).navigate(R.id.formsFragment, args)
    }

    private fun updateDonationExamples(data: GiveViewModel.DonationExampleLiveData) {
        examples_amount_giveTab.setText(data.amount.toString(), TextView.BufferType.EDITABLE)
        examples_description_giveTab.text = getString(data.description)
        leftChevron_giveTab.alpha = if (data.leftArrowEnabled) resources.getFloatDimension(R.dimen.button_active_alpha) else resources.getFloatDimension(R.dimen.button_inactive_alpha)
        rightChevron_giveTab.alpha = if (data.rightArrowEnabled) resources.getFloatDimension(R.dimen.button_active_alpha) else resources.getFloatDimension(R.dimen.button_inactive_alpha)
    }
}