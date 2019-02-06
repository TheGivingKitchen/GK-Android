package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.radioquestion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_radio.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setPaddingDp
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

// This is currently being used as a select question view
// todo: make a select question view docs.google.com/presentation/d/1EO0VWQaoIrQXHB8EucHnPXx1IBLR0AEHc5hVTl7hiLg/edit#slide=id.g4bfbc276d8_0_94
class RadioQuestion(title: String?, answerChoices: List<String?>?, hasOtherField: Boolean?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    // todo: use merge tags in views
    private var answerChoiceViews: List<RadioAnswerChoice>?

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_radio, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_radioQuestion.setTextIfItExists(title)
        val mutableAnswerChoicesList = answerChoices?.toMutableList()
        if (hasOtherField != null && hasOtherField) {
            mutableAnswerChoicesList?.add(resources.getString(R.string.answer_choice_other))
        }

        answerChoiceViews = mutableAnswerChoicesList?.map { RadioAnswerChoice(it, context) }

        if (answerChoiceViews != null) {
            for (answerChoiceView in answerChoiceViews!!) {
                answerChoiceView.setOnClickListener {
                    onSelection(it as RadioAnswerChoice)
                }
                this.addView(answerChoiceView)
            }
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
}
