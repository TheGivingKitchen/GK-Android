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

class DateQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), DatePickerDialog.OnDateSetListener, QuestionView {
    var dateYear: Int? = null
    var dateMonth: Int? = null
    var dateDay: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_date, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_dateQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            val savedDate = q.answers!![0]
            setDate(savedDate.substring(4, 6).toInt(), savedDate.substring(6).toInt(), savedDate.substring(0, 4).toInt())
        }

        q.warning?.let {
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

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) { /* Answer format is YYYYMMDD */
        val answer = date_dateQuestion.text.toString()

        if (answer.isNotBlank()) {
            val dateComponents = answer.split("/")
            val formattedAnswer = dateComponents[2] + dateComponents[0] + dateComponents[1]

            if (q.answers == null) {
                q.answers = arrayListOf()
            }
            q.answers!!.add(formattedAnswer)

            sharedPreferences?.let {
                with(it.edit()) {
                    putString(formId + q.ID, formattedAnswer)
                    apply()
                }
            }
        }
    }
}

