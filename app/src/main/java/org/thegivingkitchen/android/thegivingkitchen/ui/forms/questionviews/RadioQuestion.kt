package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.view_question_radio.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setPaddingDp
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class RadioQuestion(title: String?, answerChoices: List<String>?, hasOtherField: Boolean?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_radio, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_radioQuestion.setTextIfItExists(title)
        val mutableAnswerChoicesList = answerChoices?.toMutableList()
        if (hasOtherField != null && hasOtherField) {
            mutableAnswerChoicesList?.add(resources.getString(R.string.answer_choice_other))
        }
        if (mutableAnswerChoicesList != null) {
            for (answerChoice in mutableAnswerChoicesList) {
                val radioAnswerChoice = CheckboxAnswerChoice(answerChoice, context)
                RxView.clicks(radioAnswerChoice)
                        .takeUntil(RxView.detaches(this))
                        .map { radioAnswerChoice }
                        .subscribe { it.clickAction() }
                this.addView(radioAnswerChoice)
            }
        }

        this.setPaddingDp(0, 0, 0, 20)
    }
}
