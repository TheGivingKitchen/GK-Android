package org.givingkitchen.android.ui.forms.questionviews.timequestion

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TimePicker
import kotlinx.android.synthetic.main.view_question_time.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists
import java.text.DecimalFormat

class TimeQuestion(val q: Question, title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), TimePickerDialog.OnTimeSetListener, QuestionView {
    var timeHour: Int? = null
    var timeMinute: Int? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_time, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_timeQuestion.setTextIfItExists(title)

        q.warning.let {
            warning_timeQuestion.text = it
            warning_timeQuestion.visibility = View.VISIBLE
        }

        answer?.let {
            val timeSections = answer.split(":")
            setTime(timeSections[0].toInt(), timeSections[1].toInt())
        }
    }

    private fun setTime(hour: Int, minute: Int) {
        timeHour = hour
        timeMinute = minute

        var timePeriod = context.getString(R.string.time_question_am)
        var formattedHour = timeHour!!
        if (timeHour!! >= 12) {
            timePeriod = context.getString(R.string.time_question_pm)
            formattedHour -= 12
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

        return Pair(unformattedHour + additionalHours, minute)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        setTime(hourOfDay, minute)
    }

    override fun getQuestion(): Question {
        return q
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) { /* answer format is HH:MM:00 */
        val answer = time_timeQuestion.text.toString()

        return if (answer.isBlank() || timeHour == null || timeMinute == null) {
            null
        } else {
            val dec = DecimalFormat("00")
            context.getString(R.string.time_question_answer_format, dec.format(timeHour), dec.format(timeMinute))
        }
    }
}
