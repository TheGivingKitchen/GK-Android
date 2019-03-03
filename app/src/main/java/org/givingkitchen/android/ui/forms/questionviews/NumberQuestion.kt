package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_number.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class NumberQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_number, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_numberQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            number_numberQuestion.setText(answer)
        }
    }

    override fun isAnswered(): Boolean {
        return number_numberQuestion.text.isNotBlank()
    }

    override fun placeUnansweredWarning() {
        warning_numberQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val answer = number_numberQuestion.text.toString()

        return if (answer.isBlank()) {
            null
        } else {
            answer
        }
    }
}