package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import rx.Observable
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.view_question_checkbox_answer.view.*
import org.thegivingkitchen.android.thegivingkitchen.R

class CheckboxAnswerChoice(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_checkbox_answer, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerChoice_checkboxAnswer.text = title
    }

    fun onClick(): Observable<Void> {
        return RxView.clicks(answerChoice_checkboxAnswer)
    }

    fun invertColors() {
        this.background
    }
}

