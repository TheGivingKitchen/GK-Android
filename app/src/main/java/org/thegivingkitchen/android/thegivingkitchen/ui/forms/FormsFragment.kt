package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.thegivingkitchen.android.thegivingkitchen.R

class FormsFragment : Fragment() {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forms, container, false)
    }
}