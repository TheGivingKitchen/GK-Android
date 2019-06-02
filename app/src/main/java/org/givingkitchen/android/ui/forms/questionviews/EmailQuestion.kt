package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_email.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class EmailQuestion(val q: Question, title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_email, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_emailQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            email_emailQuestion.setText(answer)
        }

        q.warning?.let {
            warning_emailQuestion.text = it
            warning_emailQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val answer = email_emailQuestion.text.toString()

        if (answer.isNotBlank()) {
            if (q.answers == null) {
                q.answers = arrayListOf()
            }
            q.answers!!.add(answer)

            sharedPreferences?.let {
                with(it.edit()) {
                    putString(formId + q.ID, answer)
                    apply()
                }
            }
        }
    }
}