package org.givingkitchen.android.ui.homescreen.resources.map

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import org.givingkitchen.android.R

class LocationPermissionRequestDialogFragment : AppCompatDialogFragment() {

    private var onComplete: ((Boolean) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.resources_tab_location_dialog_message)
                    .setTitle(R.string.resources_tab_location_dialog_title)
                    .setPositiveButton(R.string.resources_tab_location_dialog_positive_button) { _, _ ->
                        onComplete?.invoke(true)
                    }
                    .setNegativeButton(R.string.resources_tab_location_dialog_negative_button) { _, _ ->
                        onComplete?.invoke(false)
                    }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnCompleteListener(onComplete: (Boolean) -> Unit) {
        this.onComplete = onComplete
    }
}