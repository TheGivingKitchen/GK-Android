package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.checkboxquestion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_checkbox.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.QuestionView
import org.thegivingkitchen.android.thegivingkitchen.util.setPaddingDp
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class CheckboxQuestion(title: String?, answerChoices: List<String?>?, hasOtherField: Boolean?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: use merge tags in views
    private var answerChoiceViews: List<CheckboxAnswerChoice>?

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_checkbox, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_checkboxQuestion.setTextIfItExists(title)
        val mutableAnswerChoicesList = answerChoices?.toMutableList()
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

    override fun isAnswered(): Boolean {
        if (answerChoiceViews.isNullOrEmpty()) {
            return true
        } else {
            for (checkboxAnswerChoiceView in answerChoiceViews!!) {
                if (checkboxAnswerChoiceView.isChecked()) {
                    return true
                }
            }
        }
        return false
    }

    override fun placeUnansweredWarning() {
        warning_checkboxQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val selectedCheckboxes = arrayListOf<String>()

        if (answerChoiceViews.isNullOrEmpty()) {
            return null
        } else {
            for (checkboxAnswerChoiceView in answerChoiceViews!!) {
                if (checkboxAnswerChoiceView.isChecked()) {
                    selectedCheckboxes.add(checkboxAnswerChoiceView.title!!)
                }
            }
        }

        return if (selectedCheckboxes.isNotEmpty()) {
            selectedCheckboxes.joinToString()
        } else {
            null
        }
    }
}
