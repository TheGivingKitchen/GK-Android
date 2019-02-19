package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import kotlinx.android.synthetic.main.view_question_time.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.QuestionView
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class TimeQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), TimePickerDialog.OnTimeSetListener, QuestionView {
    var timeHour: Int? = null
    var timeMinute: Int? = null

    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_time, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_timeQuestion.setTextIfItExists(title)
        setTime(11,0, "am")
    }

    fun setTime(hour: Int, minute: Int, timePeriod: String) {
        val formattedMinute = String.format("%02d", minute)

        time_timeQuestion.text = context.getString(R.string.time_question_time, hour, formattedMinute, timePeriod)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timeHour = hourOfDay
        timeMinute = minute

        var timePeriod = context.getString(R.string.time_question_am)
        var formattedHour = timeHour!!
        if (timeHour!! >= 12) {
            timePeriod = context.getString(R.string.time_question_pm)
            formattedHour-=12
        }
        if (formattedHour == 0) {
            formattedHour = 12
        }
        setTime(formattedHour, timeMinute!!, timePeriod)
    }

    override fun isAnswered(): Boolean {
        return true
    }

    override fun placeUnansweredWarning() {
        warning_timeQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val answer = time_timeQuestion.text.toString()

        return if (answer.isBlank()) {
            null
        } else {
            answer
        }
    }
}



