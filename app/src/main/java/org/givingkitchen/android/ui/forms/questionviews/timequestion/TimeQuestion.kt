package org.givingkitchen.android.ui.forms.questionviews.timequestion

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TimePicker
import kotlinx.android.synthetic.main.view_question_time.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.setTextIfItExists

class TimeQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), TimePickerDialog.OnTimeSetListener, QuestionView {
    var timeHour: Int? = null
    var timeMinute: Int? = null

    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_time, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_timeQuestion.setTextIfItExists(title)

        if (answer.isNullOrBlank()) {
            setTime(11,0)
        } else {
            // an example answer is "5:30 pm"
            val firstSection = answer.split(":")
            val secondSection = firstSection[1].split(" ")

            val unformattedTime = getUnformattedTime(firstSection[0].toInt(), secondSection[0].toInt(), secondSection[1])
            setTime(unformattedTime.first, unformattedTime.second)
        }
    }

    private fun setTime(hour: Int, minute: Int) {
        timeHour = hour
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

        val formattedMinute = String.format("%02d", minute)

        time_timeQuestion.text = context.getString(R.string.time_question_time, formattedHour, formattedMinute, timePeriod)
    }

    private fun getUnformattedTime(hour: Int, minute: Int, timePeriod: String): Pair<Int, Int> {
        val unformattedHour = if (timePeriod == context.getString(R.string.time_question_am) && hour == 12) 0 else hour
        val additionalHours = if (timePeriod == context.getString(R.string.time_question_pm) && hour != 12) 12 else 0

        return Pair(unformattedHour+additionalHours, minute)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        setTime(hourOfDay, minute)
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



