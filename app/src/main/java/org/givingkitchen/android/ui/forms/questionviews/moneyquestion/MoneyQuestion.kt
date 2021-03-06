package org.givingkitchen.android.ui.forms.questionviews.moneyquestion

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_money.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.QuestionView
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class MoneyQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_money, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_moneyQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            amount_moneyQuestion.setText(q.answers!![q.ID])
        }

        q.warning?.let {
            warning_moneyQuestion.text = it
            warning_moneyQuestion.visibility = View.VISIBLE
        }

        // todo: when focus is lost, add a dollar sign to the beginning of the amount
        /* amount_moneyQuestion.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    val currentText = amount_moneyQuestion.text
                    if (!currentText.isNullOrBlank() && currentText.length > 2 && currentText[0] == '$') {
                        amount_moneyQuestion.setText(currentText.subSequence(1, currentText.length-1))
                    }
                }
                false -> {
                    amount_moneyQuestion.text = "hello" // amount_moneyQuestion.text.insert(0, "$")
                }
            }
        }*/

        // todo: format currency properly
        // amount_moneyQuestion.addTextChangedListener(CurrencyInputFormatter(amount_moneyQuestion, "#,###"))
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val answer = amount_moneyQuestion.text.toString()

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
