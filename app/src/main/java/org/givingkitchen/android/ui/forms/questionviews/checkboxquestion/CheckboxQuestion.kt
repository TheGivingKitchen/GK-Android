package org.givingkitchen.android.ui.forms.questionviews.checkboxquestion

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_checkbox.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setPaddingDp
import org.givingkitchen.android.util.setTextIfItExists

class CheckboxQuestion(val q: Question, title: String?, answerChoices: List<String?>?, hasOtherField: Boolean?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    private var answerChoiceViews: List<CheckboxAnswerChoice>?

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_checkbox, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_checkboxQuestion.setTextIfItExists(title)
        val mutableAnswerChoicesList = answerChoices?.toMutableList()

        q.warning?.let {
            warning_checkboxQuestion.text = it
            warning_checkboxQuestion.visibility = View.VISIBLE
        }

        if (hasOtherField != null && hasOtherField) {
            mutableAnswerChoicesList?.add(resources.getString(R.string.answer_choice_other))
        }

        answerChoiceViews = mutableAnswerChoicesList?.map {
            CheckboxAnswerChoice(it, it == answer, context)
        }

        if (answerChoiceViews != null) {
            for (answerChoiceView in answerChoiceViews!!) {
                answerChoiceView.setOnClickListener {
                    (it as CheckboxAnswerChoice).clickAction()
                }
                this.addView(answerChoiceView)
            }
        }

        this.setPaddingDp(0, 0, 0, 20)
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val selectedCheckboxes = arrayListOf<String>()
        if (answerChoiceViews != null && answerChoiceViews!!.isNotEmpty()) {
            for (checkboxAnswerChoiceView in answerChoiceViews!!) {
                if (checkboxAnswerChoiceView.isChecked()) {
                    selectedCheckboxes.add(checkboxAnswerChoiceView.title!!)
                }
            }
        }

        if (selectedCheckboxes.isNotEmpty()) {
            val answer = selectedCheckboxes.joinToString()

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
