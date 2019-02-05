package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class TimePickerFragment: DialogFragment() {
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private var hour: Int = 0
    private var minute: Int = 0

    fun newInstance(listener: TimePickerDialog.OnTimeSetListener, selectedHour: Int? = null, selectedMinute: Int? = null) {
        timeSetListener = listener

        if (selectedHour != null) {
            hour = selectedHour
        } else {
            hour = 11
        }
        if (selectedMinute != null) {
            minute = selectedMinute
        } else {
            minute = 0
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context!!, timeSetListener, hour, minute, false)
    }
}