package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_date.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import java.util.*

class DateQuestion(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle){
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_date, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        title_dateQuestion.text = title
        val calendar = Calendar.getInstance()
        setDate(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))
    }

    fun setDate(month: Int, day: Int, year: Int) {
        date_dateQuestion.text = context.getString(R.string.date_question_date, month+1, day, year)
    }
}



