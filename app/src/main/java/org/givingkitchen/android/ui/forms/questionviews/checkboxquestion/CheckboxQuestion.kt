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

class CheckboxQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    private var answerChoiceViews: List<CheckboxAnswerChoice>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_checkbox, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_checkboxQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        q.SubFields?.let { subfields ->
            val mutableAnswerChoicesList = subfields.map { it.Label }.toMutableList()

            if (q.HasOtherField != null && q.HasOtherField) {
                mutableAnswerChoicesList.add(resources.getString(R.string.answer_choice_other))
            }

            val savedChoicesSet = HashSet<String>()
            if (!q.answers.isNullOrEmpty()) {
                savedChoicesSet.addAll(q.answers!![0].split(","))
            }

            answerChoiceViews = mutableAnswerChoicesList.map { title ->
                CheckboxAnswerChoice(title, savedChoicesSet.contains(title), context)
            }

            if (answerChoiceViews != null) {
                for (answerChoiceView in answerChoiceViews!!) {
                    answerChoiceView.setOnClickListener {
                        (it as CheckboxAnswerChoice).clickAction()
                    }
                    this.addView(answerChoiceView)
                }
            }
        }

        q.warning?.let {
            warning_checkboxQuestion.text = it
            warning_checkboxQuestion.visibility = View.VISIBLE
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
