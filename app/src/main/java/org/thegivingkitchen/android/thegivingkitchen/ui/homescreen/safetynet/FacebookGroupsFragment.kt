package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.thegivingkitchen.android.thegivingkitchen.R

class FacebookGroupsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_facebook_groups, container, false)
    }
}