package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.radioquestion

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_radio_answer.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class RadioAnswerChoice(val title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_radio_answer, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerChoice_radioAnswer.setTextIfItExists(title)
        this.orientation = VERTICAL
    }

    var currentSelectedState: Boolean = false
        set(value) {
            field = value
            when (value) {
                true -> {
                    answerChoice_radioAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.gk_blue))
                    answerChoice_radioAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                false -> {
                    answerChoice_radioAnswer.background = ContextCompat.getDrawable(context, R.drawable.checkbox_answer_border)
                    answerChoice_radioAnswer.setTextColor(ContextCompat.getColor(context, R.color.gk_blue))
                }
            }
        }

    fun clickAction() {
        currentSelectedState = !currentSelectedState
        if (title != null && title == resources.getString(R.string.answer_choice_other)) {
            when (currentSelectedState) {
                true -> {
                    answerChoice_radioOtherAnswer.visibility = View.VISIBLE
                }
                false -> {
                    answerChoice_radioOtherAnswer.visibility = View.GONE
                }
            }
        }
    }

    fun isChecked(): Boolean {
        return currentSelectedState
    }
}

