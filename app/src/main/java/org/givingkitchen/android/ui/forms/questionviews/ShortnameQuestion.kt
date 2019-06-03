package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_shortname.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class ShortnameQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_shortname, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_shortnameQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            firstName_shortnameQuestion.setText(q.answers!![q.ID])
            lastName_shortnameQuestion.setText(q.answers!![getNextFieldId(q.ID, context)])
        }

        q.warning?.let {
            warning_shortnameQuestion.text = it
            warning_shortnameQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        saveTextFieldAnswer(firstName_shortnameQuestion.text.toString(), sharedPreferences, formId, q.ID)
        saveTextFieldAnswer(lastName_shortnameQuestion.text.toString(), sharedPreferences, formId, getNextFieldId(q.ID, context))
    }

    private fun saveTextFieldAnswer(answer: String, sharedPreferences: SharedPreferences?, formId: String, uniqueFieldId: String) {
        if (q.answers == null) {
            q.answers = HashMap()
        }
        q.answers!![uniqueFieldId] = answer

        sharedPreferences?.let {
            with(it.edit()) {
                putString(formId + uniqueFieldId, answer)
                apply()
            }
        }
    }
}
