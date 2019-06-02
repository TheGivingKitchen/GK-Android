package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_phone.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class PhoneQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: format displayed phone number properly; watch out for non-US phone number formats
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_phone, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_phoneQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            phoneNumber_phoneQuestion.setText(q.answers!![q.ID])
        }

        q.warning?.let {
            warning_phoneQuestion.text = it
            warning_phoneQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val answer = phoneNumber_phoneQuestion.text.toString()

        if (q.answers == null) {
            q.answers = HashMap()
        }
        q.answers!![q.ID] = answer

        sharedPreferences?.let {
            with(it.edit()) {
                putString(formId + q.ID, answer)
                apply()
            }
        }
    }
}