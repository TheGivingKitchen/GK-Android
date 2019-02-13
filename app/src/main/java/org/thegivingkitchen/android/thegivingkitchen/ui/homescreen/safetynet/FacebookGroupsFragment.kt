package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_facebook_groups.*
import android.content.Intent
import android.net.Uri
import org.thegivingkitchen.android.thegivingkitchen.R


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
        metroAtlTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(metroAtlGroupId) }
        athensTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(athensGroupId) }
        columbusTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(columbusGroupId) }
        romeTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(romeGroupId) }
        maconTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(maconGroupId) }
        gainesvilleTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(gainesvilleGroupId) }
        northGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(northGaGroupId) }
        southGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(southGaGroupId) }
        coastalGaTouchTarget_facebookGroups.setOnClickListener { openFacebookLink(coastalGaGroupId) }
    }

    private fun openFacebookLink(groupId: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$facebookBaseUrl$groupId")))
    }
}