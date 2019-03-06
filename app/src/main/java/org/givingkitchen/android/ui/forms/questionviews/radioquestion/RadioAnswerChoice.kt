package org.givingkitchen.android.ui.forms.questionviews.radioquestion

import android.content.Context
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_radio_answer.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class RadioAnswerChoice(val title: String?, selected: Boolean, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_radio_answer, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        answerChoice_radioAnswer.setTextIfItExists(title)
        this.orientation = VERTICAL
        if (selected) {
            clickAction()
        }
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

