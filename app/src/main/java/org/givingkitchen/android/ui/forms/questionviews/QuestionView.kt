package org.givingkitchen.android.ui.forms.questionviews

import org.givingkitchen.android.ui.forms.Question

interface QuestionView {

    fun getAnswer(): String? /* returns null if the question hasn't been answered */

    fun getQuestion(): Question
}