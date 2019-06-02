package org.givingkitchen.android.ui.forms.questionviews.radioquestion

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_radio.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setPaddingDp
import org.givingkitchen.android.util.setTextIfItExists

// This is currently being used as a select question view
// todo: make a select question view https://docs.google.com/presentation/d/1EO0VWQaoIrQXHB8EucHnPXx1IBLR0AEHc5hVTl7hiLg/edit?usp=sharing
class RadioQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), QuestionView {
    private var answerChoiceViews: List<RadioAnswerChoice>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_radio, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_radioQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))


        q.Choices?.let { choices ->
            val mutableAnswerChoicesList = choices.map { it.Label }.toMutableList()

            if (q.HasOtherField != null && q.HasOtherField) {
                mutableAnswerChoicesList.add(resources.getString(R.string.answer_choice_other))
            }

            answerChoiceViews = mutableAnswerChoicesList.map { title ->
                val matchesSavedChoice = if (!q.answers.isNullOrEmpty()) {
                    title == q.answers!![q.ID]
                } else {
                    false
                }

                RadioAnswerChoice(title, matchesSavedChoice, context)
            }

            if (answerChoiceViews != null) {
                for (answerChoiceView in answerChoiceViews!!) {
                    answerChoiceView.setOnClickListener {
                        onSelection(it as RadioAnswerChoice)
                    }
                    this.addView(answerChoiceView)
                }
            }
        }

        q.warning?.let {
            warning_radioQuestion.text = it
            warning_radioQuestion.visibility = View.VISIBLE
        }

        this.setPaddingDp(0, 0, 0, 20)
    }

    private fun onSelection(radioAnswerChoice: RadioAnswerChoice) {
        for (answerChoiceView in answerChoiceViews!!) {
            if (answerChoiceView.currentSelectedState) {
                answerChoiceView.clickAction()
            }
        }
        radioAnswerChoice.clickAction()
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        var answer = ""
        if (answerChoiceViews != null && answerChoiceViews!!.isNotEmpty()) {
            for (radioAnswerChoiceView in answerChoiceViews!!) {
                if (radioAnswerChoiceView.isChecked()) {
                    answer = radioAnswerChoiceView.title ?: ""
                }
            }
        }

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
