package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_date.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.QuestionView
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.util.*

class DateQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), DatePickerDialog.OnDateSetListener, QuestionView {
    // todo: use merge tags in views
    var dateYear: Int? = null
    var dateMonth: Int? = null
    var dateDay: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_date, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_dateQuestion.setTextIfItExists(title)
        val calendar = Calendar.getInstance()
        if (answer.isNullOrBlank()) {
            setDate(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))
        } else {
            val a = answer.split("/")
            setDate(a[0].toInt(), a[1].toInt(), a[2].toInt())
        }
    }

    private fun setDate(month: Int, day: Int, year: Int) {
        date_dateQuestion.text = context.getString(R.string.date_question_date, month+1, day, year)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateYear = year
        dateMonth = month
        dateDay = dayOfMonth

        setDate(dateMonth!!, dateDay!!, dateYear!!)
    }

    override fun isAnswered(): Boolean {
        return true
    }

    override fun placeUnansweredWarning() {
        warning_dateQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val answer = date_dateQuestion.text.toString()

        return if (answer.isBlank()) {
            null
        } else {
            answer
        }
    }
}



