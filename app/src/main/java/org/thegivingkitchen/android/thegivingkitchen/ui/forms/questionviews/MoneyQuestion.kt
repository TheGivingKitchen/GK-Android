package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_money.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class MoneyQuestion(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_money, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        title_moneyQuestion.setTextIfItExists(title)
        amount_moneyQuestion.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    val currentText = amount_moneyQuestion.text
                    if (!currentText.isNullOrBlank() && currentText.length > 2 && currentText[0] == '$') {
                        amount_moneyQuestion.setText(currentText.subSequence(1, currentText.length-1))
                    }
                }
                false -> {
                    title_moneyQuestion.text = "lost focus"
                }
            }
        }
        // amount_moneyQuestion.addTextChangedListener(CurrencyInputFormatter(amount_moneyQuestion))
    }
}
/*
class CurrencyInputFormatter(val editText: EditText): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}*/