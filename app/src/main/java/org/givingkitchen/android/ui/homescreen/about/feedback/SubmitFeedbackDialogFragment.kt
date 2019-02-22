package org.givingkitchen.android.ui.homescreen.about.feedback

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import org.givingkitchen.android.R

class SubmitFeedbackDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.feedback_dialog_description)
                    .setTitle(R.string.feedback_dialog_header)
                    .setPositiveButton(R.string.feedback_dialog_button,
                            // todo: make FeedbackFragment call onBackPressed upon dismissing this dialog
                            DialogInterface.OnClickListener { dialog, id ->

                            })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}