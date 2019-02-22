package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_address.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.util.setTextIfItExists

class AddressQuestion(title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: use merge tags in views
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_address, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = VERTICAL
        title_addressQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            // amount_moneyQuestion.setText(answer)

        }
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
        val address = arrayListOf<String>()

        val streetAddressFieldValue = getTextFieldValue(streetAddressField_addressQuestion.text)
        if (streetAddressFieldValue != null) {
            address.add(streetAddressFieldValue)
        } else {
            return null
        }

        // Address Line Two does not need to be filled out
        val addressLineTwoAnswer = addressLineTwoField_addressQuestion.text
        if (addressLineTwoAnswer.isNotBlank()) {
            address.add(addressLineTwoAnswer.toString())
        }

        val cityFieldValue = getTextFieldValue(cityField_addressQuestion.text)
        if (cityFieldValue != null) {
            address.add(cityFieldValue)
        } else {
            return null
        }

        val stateFieldValue = getTextFieldValue(stateField_addressQuestion.text)
        if (stateFieldValue != null) {
            address.add(stateFieldValue)
        } else {
            return null
        }

        val zipcodeFieldValue = getTextFieldValue(zipcodeField_addressQuestion.text)
        if (zipcodeFieldValue != null) {
            address.add(zipcodeFieldValue)
        } else {
            return null
        }

        val fullAddress = address.joinToString()
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
