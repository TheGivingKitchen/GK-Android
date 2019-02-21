package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
// import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.thegivingkitchen.android.thegivingkitchen.R

class ResourceProviderDetailsFragment : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_facebook_groups, container, false)
    }
}