package org.givingkitchen.android.ui.homescreen.about

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_how_it_works.*
import org.givingkitchen.android.R

class HowItWorksFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_how_it_works, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoView_howItWorks.setImageResource(R.drawable.pic_how_it_works)
        closeButton_howItWorks.setOnClickListener { findNavController().navigateUp() }
    }
}