package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_resource_provider_details.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class ResourceProviderDetailsFragment : BottomSheetDialogFragment() {
    companion object {
        const val providerArg = "provider"

        fun newInstance(provider: SocialServiceProvider): ResourceProviderDetailsFragment {
            val resourceProviderDetailsFragment = ResourceProviderDetailsFragment()
            val args = Bundle()
            args.putParcelable(providerArg, provider)
            resourceProviderDetailsFragment.arguments = args
            return resourceProviderDetailsFragment
        }
    }

    private lateinit var provider: SocialServiceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            provider = arguments!!.getParcelable(providerArg)!!
        }
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
            allButtonsGone = false
        }

        val directions = provider.address
        if (directions.isNullOrBlank()) {
            directionsDivider_resourceProviderBottomSheet.visibility = View.GONE
            directionsButton_resourceProviderBottomSheet.visibility = View.GONE
            address_resourceProviderBottomSheet.visibility = View.GONE
        } else {
            address_resourceProviderBottomSheet.text = directions
            allButtonsGone = false
        }

        val phone = provider.phone
        if (phone.isNullOrBlank()) {
            callTopDivider_resourceProviderBottomSheet.visibility = View.GONE
            callButton_resourceProviderBottomSheet.visibility = View.GONE
            phone_resourceProviderBottomSheet.visibility = View.GONE
        } else {
            phone_resourceProviderBottomSheet.text = phone
            allButtonsGone = false
        }

        if (allButtonsGone) {
            callBottomDivider_resourceProviderBottomSheet.visibility = View.GONE
        }
    }
}