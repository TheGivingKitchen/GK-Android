package org.givingkitchen.android.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment: DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun newInstance(listener: DatePickerDialog.OnDateSetListener, selectedYear: Int?, selectedMonth: Int?, selectedDay: Int?): DatePickerFragment {
        dateSetListener = listener

        val calendar = Calendar.getInstance()
        year = selectedYear ?: calendar.get(Calendar.YEAR)
        month = selectedMonth ?: calendar.get(Calendar.MONTH)
        day = selectedDay ?: calendar.get(Calendar.DAY_OF_MONTH)

        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, dateSetListener, year, month, day)
    }
}