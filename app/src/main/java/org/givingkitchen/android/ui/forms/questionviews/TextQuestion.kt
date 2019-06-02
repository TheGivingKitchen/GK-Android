package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_text.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class TextQuestion(val q: Question, title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_text, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_textQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            text_textQuestion.setText(answer)
        }

        q.warning?.let {
            warning_textQuestion.text = it
            warning_textQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val answer = text_textQuestion.text.toString()

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