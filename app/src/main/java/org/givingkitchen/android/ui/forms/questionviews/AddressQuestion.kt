package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_question_address.view.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.util.convertToDp
import org.givingkitchen.android.util.setTextIfItExists

class AddressQuestion(val q: Question, title: String?, answer: String? = null, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), QuestionView {
    // todo: prefill this question from shared prefs
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_address, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0,0,0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_addressQuestion.setTextIfItExists(title)
        if (!answer.isNullOrBlank()) {
            // amount_moneyQuestion.setText(answer)

        }

        q.warning.let {
            warning_addressQuestion.text = it
            warning_addressQuestion.visibility = View.VISIBLE
        }
    }

    override fun getQuestion(): Question {
        return q
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
