package org.givingkitchen.android.ui.forms.questionviews.timequestion

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class TimePickerFragment: DialogFragment() {
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private var hour: Int = 0
    private var minute: Int = 0

    fun newInstance(listener: TimePickerDialog.OnTimeSetListener, selectedHour: Int? = null, selectedMinute: Int? = null): TimePickerFragment {
        timeSetListener = listener

        hour = selectedHour ?: 11
        minute = selectedMinute ?: 0

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context!!, timeSetListener, hour, minute, false)
    }
}