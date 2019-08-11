package org.givingkitchen.android.ui.homescreen.resources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class ResourcesIndexFragment: BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): ResourcesIndexFragment {
            return ResourcesIndexFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(org.givingkitchen.android.R.layout.fragment_resources_index, container, false)
    }


}