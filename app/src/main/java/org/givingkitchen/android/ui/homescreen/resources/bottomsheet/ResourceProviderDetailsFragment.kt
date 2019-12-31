package org.givingkitchen.android.ui.homescreen.resources.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_resource_provider_details.*
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.analytics.Events
import org.givingkitchen.android.analytics.Parameter
import org.givingkitchen.android.ui.homescreen.resources.ResourceProvider
import org.givingkitchen.android.util.CustomTabs
import org.givingkitchen.android.util.setTextIfItExists

class ResourceProviderDetailsFragment private constructor(): BottomSheetDialogFragment() {
    companion object {
        const val providerArg = "provider"

        fun newInstance(provider: ResourceProvider): ResourceProviderDetailsFragment {
            return ResourceProviderDetailsFragment().apply {
                this.arguments = Bundle().apply {
                    this.putParcelable(providerArg, provider)
                }
            }
        }
    }

    private lateinit var provider: ResourceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provider = arguments!!.getParcelable(providerArg)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resource_provider_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_resourceProviderBottomSheet.setTextIfItExists(provider.name)
        category_resourceProviderBottomSheet.setTextIfItExists(provider.category)
        description_resourceProviderBottomSheet.setTextIfItExists(provider.description)

        var allButtonsGone = true
        val websiteLink = provider.website
        if (websiteLink.isNullOrBlank()) {
            websiteDivider_resourceProviderBottomSheet.visibility = View.GONE
            websiteButton_resourceProviderBottomSheet.visibility = View.GONE
            website_resourceProviderBottomSheet.visibility = View.GONE
        } else {
            website_resourceProviderBottomSheet.text = websiteLink
            websiteButton_resourceProviderBottomSheet.setOnClickListener(websiteButtonClickListener)
            allButtonsGone = false
        }

        val directions = combinedAddress(provider.address1, provider.address2)
        if (directions.isNullOrBlank()) {
            directionsDivider_resourceProviderBottomSheet.visibility = View.GONE
            directionsButton_resourceProviderBottomSheet.visibility = View.GONE
            address_resourceProviderBottomSheet.visibility = View.GONE
        } else {
            address_resourceProviderBottomSheet.text = directions
            directionsButton_resourceProviderBottomSheet.setOnClickListener(directionsButtonClickListener)
            allButtonsGone = false
        }

        val phone = provider.phone
        if (phone.isNullOrBlank()) {
            callTopDivider_resourceProviderBottomSheet.visibility = View.GONE
            callButton_resourceProviderBottomSheet.visibility = View.GONE
            phone_resourceProviderBottomSheet.visibility = View.GONE
        } else {
            phone_resourceProviderBottomSheet.text = phone
            callButton_resourceProviderBottomSheet.setOnClickListener(phoneButtonClickListener)
            allButtonsGone = false
        }

        if (allButtonsGone) {
            callBottomDivider_resourceProviderBottomSheet.visibility = View.GONE
        }

        val counties = provider.countiesServed
        if (counties != null) {
            counties_resourceProviderBottomSheet.text = getString(R.string.safetynet_bottomsheet_serves_counties, counties.joinToString())
        } else {
            counties_resourceProviderBottomSheet.visibility = View.GONE
            callBottomDivider_resourceProviderBottomSheet.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        BottomSheetBehavior.from(dialog!!.findViewById<FrameLayout>(R.id.design_bottom_sheet))
                .state = BottomSheetBehavior.STATE_EXPANDED
    }

    private val websiteButtonClickListener = View.OnClickListener {
        Analytics.logEvent(Events.SAFETY_NET_VISIT_WEBSITE, providerAnalytics(provider))
        CustomTabs.openCustomTab(context, provider.website!!)
    }

    private val directionsButtonClickListener = View.OnClickListener {
        Analytics.logEvent(Events.SAFETY_NET_VISIT_ADDRESS, providerAnalytics(provider))
        combinedAddress(provider.address1, provider.address2)?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + it)))
        }
    }

    private val phoneButtonClickListener = View.OnClickListener {
        Analytics.logEvent(Events.SAFETY_NET_CALL_PHONE, providerAnalytics(provider))
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + provider.phone!!)))
    }

    private fun providerAnalytics(provider: ResourceProvider): Map<Parameter, String> {
        return mapOf(Parameter.SAFETY_NET_NAME to provider.name)
    }

    private fun combinedAddress(address1: String?, address2: String?): String? {
        if (address1.isNullOrBlank()) {
            return null
        }
        var address = address1
        if (!address2.isNullOrBlank()) {
            val intermediateSpace = if (address.last() == ' ') {
                ""
            } else {
                " "
            }
            address += intermediateSpace + address2
        }
        return address
    }
}