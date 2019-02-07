package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

interface QuestionView {

    fun isAnswered(): Boolean

    fun placeUnansweredWarning()
}