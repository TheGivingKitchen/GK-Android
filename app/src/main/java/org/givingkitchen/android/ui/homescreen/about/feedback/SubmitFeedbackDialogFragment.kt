package org.givingkitchen.android.ui.homescreen.about.feedback

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import org.givingkitchen.android.R

class SubmitFeedbackDialogFragment : AppCompatDialogFragment() {

    private var onComplete: ((Boolean) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.feedback_dialog_description)
                    .setTitle(R.string.feedback_dialog_header)
                    .setPositiveButton(R.string.feedback_dialog_button) { _, _ ->
                        onComplete?.invoke(true)
                    }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setOnCompleteListener(onComplete: (Boolean) -> Unit) {
        this.onComplete = onComplete
    }
}