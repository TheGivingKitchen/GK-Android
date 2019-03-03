package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_phone.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class PhoneQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_phone, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_phoneQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            phoneNumber_phoneQuestion.setText(answer)
        }
    }

    override fun isAnswered(): Boolean {
        return phoneNumber_phoneQuestion.text.isNotBlank()
    }

    override fun placeUnansweredWarning() {
        warning_phoneQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val answer = phoneNumber_phoneQuestion.text.toString()

        return if (answer.isBlank()) {
            null
        } else {
            answer
        }
    }
}