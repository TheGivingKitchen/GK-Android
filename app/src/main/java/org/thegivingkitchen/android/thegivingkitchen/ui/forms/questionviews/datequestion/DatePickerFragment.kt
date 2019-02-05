package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import java.util.*

class DatePickerFragment: DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun newInstance(listener: DatePickerDialog.OnDateSetListener, selectedYear: Int? = null, selectedMonth: Int? = null, selectedDay: Int? = null) {
        dateSetListener = listener

        val calendar = Calendar.getInstance()
        if (selectedYear != null) {
            year = selectedYear
        } else {
            year = calendar.get(Calendar.YEAR)
        }
        if (selectedMonth != null) {
            month = selectedMonth
        } else {
            month = calendar.get(Calendar.MONTH)
        }
        if (selectedDay != null) {
            day = selectedDay
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, dateSetListener, year, month, day)
    }
}