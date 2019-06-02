package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_url.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class UrlQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_url, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_urlQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            url_urlQuestion.setText(q.answers!![0])
        }


        q.warning?.let {
            warning_urlQuestion.text = it
            warning_urlQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val answer = url_urlQuestion.text.toString()

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