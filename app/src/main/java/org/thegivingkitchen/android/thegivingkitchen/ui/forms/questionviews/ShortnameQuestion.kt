package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_shortname.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.lang.StringBuilder

class ShortnameQuestion(title: String?, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_shortname, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_shortnameQuestion.setTextIfItExists(title)
    }

    override fun isAnswered(): Boolean {
        return (firstName_shortnameQuestion.text.isNotBlank() && lastName_shortnameQuestion.text.isNotBlank())
    }

    override fun placeUnansweredWarning() {
        warning_shortnameQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val address = StringBuilder()

        address.append(getTextFieldValue(firstName_shortnameQuestion.text))
        address.append(getTextFieldValue(lastName_shortnameQuestion.text))

        val fullName = address.toString()
        return if (fullName.isNotBlank()) {
            fullName
        } else {
            null
        }
    }

    private fun getTextFieldValue(answer: CharSequence): String? {
        return if (answer.isNotBlank()) {
            answer.toString()
        } else {
            null
        }
    }
}