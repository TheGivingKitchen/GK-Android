package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_select.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.util.*

class DateQuestion(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), DatePickerDialog.OnDateSetListener{
    // todo: use merge tags in views
    var dateYear: Int? = null
    var dateMonth: Int? = null
    var dateDay: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_select, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        title_selectQuestion.setTextIfItExists(title)
        val calendar = Calendar.getInstance()
        setDate(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))
    }

    fun setDate(month: Int, day: Int, year: Int) {
        selection_selectQuestion.text = context.getString(R.string.date_question_date, month+1, day, year)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateYear = year
        dateMonth = month
        dateDay = dayOfMonth

        setDate(dateMonth!!, dateDay!!, dateYear!!)
    }
}



