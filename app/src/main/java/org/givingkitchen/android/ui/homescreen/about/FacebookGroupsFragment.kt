package org.givingkitchen.android.ui.homescreen.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_facebook_groups.*
import android.content.Intent
import android.net.Uri
import androidx.navigation.fragment.findNavController
import org.givingkitchen.android.R
import org.givingkitchen.android.analytics.Analytics
import org.givingkitchen.android.analytics.Events
import org.givingkitchen.android.analytics.Parameter

class FacebookGroupsFragment : Fragment() {

    companion object {
        const val facebookBaseUrl = "https://www.facebook.com/groups/"
        const val metroAtlGroupId = "1426736404312089"
        const val athensGroupId = "187999045361005"
        const val columbusGroupId = "186740988816183"
        const val romeGroupId = "182229385767905"
        const val maconGroupId = "218626942273765"
        const val gainesvilleGroupId = "897787080404359"
        const val northGaGroupId = "1836033839791219"
        const val southGaGroupId = "1763853010365060"
        const val coastalGaGroupId = "197473227547650"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_facebook_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upButton_facebookGroups.setOnClickListener { navigateUp() }
        metroAtlTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(metroAtlGroupId, "metro_atlanta") }
        athensTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(athensGroupId, "athens_ga") }
        columbusTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(columbusGroupId, "columbus_ga") }
        romeTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(romeGroupId, "rome_ga") }
        maconTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(maconGroupId, "macon_ga") }
        gainesvilleTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(gainesvilleGroupId, "gainesville_ga") }
        northGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(northGaGroupId, "north_ga") }
        southGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(southGaGroupId, "south_ga") }
        coastalGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(coastalGaGroupId, "coastal_ga") }
    }

    private fun openFacebookLink(groupId: String, analyticsName: String) {
        Analytics.logEvent(Events.SAFETY_NET_FACEBOOK_GROUP_VISIT, mapOf(Parameter.SAFETY_NET_FACEBOOK_GROUP_NAME to analyticsName))
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$facebookBaseUrl$groupId")))
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }
}