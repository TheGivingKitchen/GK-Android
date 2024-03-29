package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
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

class AddressQuestion(val q: Question, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle), QuestionView {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_address, this, true)
        val customLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        customLayoutParams.setMargins(0, 0, 0, convertToDp(20, resources))
        layoutParams = customLayoutParams
        this.orientation = VERTICAL
        title_addressQuestion.setTextIfItExists(formatTitle(q.Title, q.IsRequired))

        if (!q.answers.isNullOrEmpty()) {
            val streetAddressFieldId = q.ID
            streetAddressField_addressQuestion.setText(q.answers!![streetAddressFieldId])

            val addressLineTwoFieldId = getNextFieldId(streetAddressFieldId, context)
            addressLineTwoField_addressQuestion.setText(q.answers!![addressLineTwoFieldId])

            val cityFieldId = getNextFieldId(addressLineTwoFieldId, context)
            cityField_addressQuestion.setText(q.answers!![cityFieldId])

            val stateFieldId = getNextFieldId(cityFieldId, context)
            stateField_addressQuestion.setText(q.answers!![stateFieldId])

            val zipcodeFieldId = getNextFieldId(stateFieldId, context)
            zipcodeField_addressQuestion.setText(q.answers!![zipcodeFieldId])
        }

        q.warning?.let {
            warning_addressQuestion.text = it
            warning_addressQuestion.visibility = View.VISIBLE
        }
    }

    override fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?) {
        val streetAddressFieldId = q.ID
        saveTextFieldAnswer(streetAddressField_addressQuestion.text.toString(), sharedPreferences, formId, streetAddressFieldId)

        val addressLineTwoFieldId = getNextFieldId(streetAddressFieldId, context)
        saveTextFieldAnswer(addressLineTwoField_addressQuestion.text.toString(), sharedPreferences, formId, addressLineTwoFieldId)

        val cityFieldId = getNextFieldId(addressLineTwoFieldId, context)
        saveTextFieldAnswer(cityField_addressQuestion.text.toString(), sharedPreferences, formId, cityFieldId)

        val stateFieldId = getNextFieldId(cityFieldId, context)
        saveTextFieldAnswer(stateField_addressQuestion.text.toString(), sharedPreferences, formId, stateFieldId)

        val zipcodeFieldId = getNextFieldId(stateFieldId, context)
        saveTextFieldAnswer(zipcodeField_addressQuestion.text.toString(), sharedPreferences, formId, zipcodeFieldId)

        val countryFieldId = getNextFieldId(zipcodeFieldId, context)
        saveTextFieldAnswer(context.getString(R.string.address_question_country_default), sharedPreferences, formId, countryFieldId)
    }

    private fun saveTextFieldAnswer(answer: String, sharedPreferences: SharedPreferences?, formId: String, uniqueFieldId: String) {
        if (q.answers == null) {
            q.answers = HashMap()
        }
        q.answers!![uniqueFieldId] = answer

        sharedPreferences?.let {
            with(it.edit()) {
                putString(formId + uniqueFieldId, answer)
                apply()
            }
        }
    }
}
