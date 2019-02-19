package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_address.view.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.lang.StringBuilder

class AddressQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_address, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_addressQuestion.setTextIfItExists(title)
    }

    override fun isAnswered(): Boolean {
        return (streetAddressField_addressQuestion.text.isNotBlank() &&
                cityField_addressQuestion.text.isNotBlank() &&
                stateField_addressQuestion.text.isNotBlank() &&
                zipcodeField_addressQuestion.text.isNotBlank())
    }

    override fun placeUnansweredWarning() {
        warning_addressQuestion.visibility = View.VISIBLE
    }

    override fun getAnswer(): String? {
        val address = StringBuilder()

        address.append(getTextFieldValue(streetAddressField_addressQuestion.text))

        // Address Line Two does not need to be filled out
        val addressLineTwoAnswer = addressLineTwoField_addressQuestion.text
        if (addressLineTwoAnswer.isNotBlank()) {
            address.append(addressLineTwoAnswer)
        }

        address.append(getTextFieldValue(cityField_addressQuestion.text))
        address.append(getTextFieldValue(stateField_addressQuestion.text))
        address.append(getTextFieldValue(zipcodeField_addressQuestion.text))

        val fullAddress = address.toString()
        return if (fullAddress.isNotBlank()) {
            fullAddress
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
