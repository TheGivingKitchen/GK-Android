package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_time.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class TimeQuestion(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle){
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_time, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        title_timeQuestion.setTextIfItExists(title)
        setTime(11,0, "am")
    }

    fun setTime(hour: Int, minute: Int, timePeriod: String) {
        val formattedMinute = String.format("%02d", minute)

        time_timeQuestion.setTextIfItExists(context.getString(R.string.time_question_time, hour, formattedMinute, timePeriod))
    }
}



