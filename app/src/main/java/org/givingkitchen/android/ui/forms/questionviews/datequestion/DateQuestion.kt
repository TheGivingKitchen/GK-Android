package org.givingkitchen.android.ui.forms.questionviews.datequestion

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_date.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists
import java.text.DecimalFormat

class DateQuestion(val q: Question, title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), DatePickerDialog.OnDateSetListener, QuestionView {
    var dateYear: Int? = null
    var dateMonth: Int? = null
    var dateDay: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_date, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_dateQuestion.setTextIfItExists(title)

        answer?.let {
            setDate(answer.substring(4, 6).toInt(), answer.substring(6).toInt(), answer.substring(0, 4).toInt())
        }

        q.warning.let {
            warning_dateQuestion.text = it
            warning_dateQuestion.visibility = View.VISIBLE
        }
    }

    private fun setDate(month: Int, dayOfMonth: Int, year: Int) {
        dateMonth = month
        dateDay = dayOfMonth
        dateYear = year

        val dec = DecimalFormat("00")
        date_dateQuestion.text = context.getString(R.string.date_question_date, dec.format(month), dec.format(dayOfMonth), dec.format(year))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        setDate(month+1, dayOfMonth, year)
    }

    override fun getQuestion(): Question {
        return q
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) { /* Answer format is YYYYMMDD */
        val answer = date_dateQuestion.text.toString()

        return if (answer.isNotBlank()) {
            val dateComponents = answer.split("/")
            dateComponents[2] + dateComponents[0] + dateComponents[1]
        } else {
            null
        }
    }
}



