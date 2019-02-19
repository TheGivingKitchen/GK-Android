package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

interface QuestionView {

    fun isAnswered(): Boolean

    fun placeUnansweredWarning()

    fun getAnswer(): String? /* returns null if the question hasn't been answered */
}