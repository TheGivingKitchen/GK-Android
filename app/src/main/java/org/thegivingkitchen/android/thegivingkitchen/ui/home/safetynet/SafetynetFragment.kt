package org.thegivingkitchen.android.thegivingkitchen.ui.home.safetynet

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.Constants.ONE_MEGABYTE
import org.thegivingkitchen.android.thegivingkitchen.util.Firebase.firebaseInstance

class SafetynetFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_safetynet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseInstance.getReferenceFromUrl("gs://thegivingkitchen-cdd28.appspot.com/safetyNet/safetyNet.json").getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val x = it
        }.addOnFailureListener {
            // todo: log error and show error state
        }
    }
}