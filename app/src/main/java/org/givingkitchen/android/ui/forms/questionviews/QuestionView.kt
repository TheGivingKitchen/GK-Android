package org.givingkitchen.android.ui.forms.questionviews

import android.content.Context
import android.content.SharedPreferences
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Question

interface QuestionView {

    fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?)

    fun getQuestion(): Question

    fun getNextFieldId(currentFieldId: String, context: Context): String {
        val fieldIdNumber = currentFieldId.split("Field")[1].toInt()
        return context.getString(R.string.forms_questions_field_id_format, fieldIdNumber+1)
    }

    fun formatTitle(title: String?, isRequired: String?): String? {
        return if (title != null && isRequired != null && isRequired == "1") "$title*" else title
    }
}