package org.givingkitchen.android.ui.forms.questionviews

import android.content.SharedPreferences
import org.givingkitchen.android.ui.forms.Question

interface QuestionView {

    fun saveAnswer(formId: String, sharedPreferences: SharedPreferences?)

    fun getQuestion(): Question
}