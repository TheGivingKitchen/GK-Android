package org.givingkitchen.android.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

class DatePickerFragment: DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun newInstance(listener: DatePickerDialog.OnDateSetListener, selectedYear: Int, selectedMonth: Int, selectedDay: Int): DatePickerFragment {
        dateSetListener = listener

        year = selectedYear
        month = selectedMonth
        day = selectedDay

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, dateSetListener, year, month, day)
    }
}