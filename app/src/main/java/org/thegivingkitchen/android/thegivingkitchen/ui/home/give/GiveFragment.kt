package org.thegivingkitchen.android.thegivingkitchen.ui.home.give

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_give.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.getFloatDimension

class GiveFragment : Fragment() {
    // todo: use url warming service for chrome custom tabs

    private lateinit var model: GiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        model = ViewModelProviders.of(this).get(GiveViewModel::class.java)
        model.getCurrentDonationData().observe(this, Observer<GiveViewModel.DonationExampleLiveData> { liveData ->
            updateDonationExamples(liveData!!)
        })
        super.onCreate(savedInstanceState)
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_give, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_donate_button_giveTab.setOnClickListener(creditCardDonationClickListener)
        recurring_donate_button_giveTab.setOnClickListener(recurringDonationClickListener)
        leftChevronClickHolder_giveTab.setOnClickListener(leftExamplesArrowClickListener)
        rightChevronClickHolder_giveTab.setOnClickListener(rightExamplesArrowClickListener)
        volunteer_button_giveTab.setOnClickListener(volunteerButtonClickListener)
        partner_button_giveTab.setOnClickListener(partnerButtonClickListener)
    }

    private val creditCardDonationClickListener = View.OnClickListener {
        CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(context!!, R.color.white))
                // todo: this does't do anything?
                .setCloseButtonIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_chevron_left))
                .build()
                .launchUrl(context, Uri.parse(model.creditCardDonationURL))
    }

    private val recurringDonationClickListener = View.OnClickListener {
        Toast.makeText(context, "recurring donate button clicked", LENGTH_SHORT).show()
    }

    private val leftExamplesArrowClickListener = View.OnClickListener {
        model.onLeftArrowClicked()
    }

    private val rightExamplesArrowClickListener = View.OnClickListener {
        model.onRightArrowClicked()
    }

    private val volunteerButtonClickListener = View.OnClickListener {
        Toast.makeText(context, "volunteer button clicked", Toast.LENGTH_SHORT).show()
    }

    private val partnerButtonClickListener = View.OnClickListener {
        Toast.makeText(context, "partner button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun updateDonationExamples(data: GiveViewModel.DonationExampleLiveData) {
        examples_amount_giveTab.setText(data.amount.toString(), TextView.BufferType.EDITABLE)
        examples_description_giveTab.text = getString(data.description)
        leftChevron_giveTab.alpha = if (data.leftArrowEnabled) resources.getFloatDimension(R.dimen.button_active_alpha) else resources.getFloatDimension(R.dimen.button_inactive_alpha)
        rightChevron_giveTab.alpha = if (data.rightArrowEnabled) resources.getFloatDimension(R.dimen.button_active_alpha) else resources.getFloatDimension(R.dimen.button_inactive_alpha)
    }
}