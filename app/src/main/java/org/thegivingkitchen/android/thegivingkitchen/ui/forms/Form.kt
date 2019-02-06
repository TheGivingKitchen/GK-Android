package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.page.QuestionType

class Form(val ID: String?,
           val FormTitle: String?,
           val FormSubtitle: String?,
           val FormMetadata: String?,
           val FormShareString: String?,
           val Pages: List<Page>? = listOf())

@Parcelize
data class Page(val pageInformation: String?,
                val questions: List<Question>? = listOf()): Parcelable

@Parcelize
data class Question(val Title: String?,
                    val IsRequired: String?,
                    val Type: QuestionType?,
                    val SubFields: List<SubField>? = listOf(),
                    val Choices: List<Choice>? = listOf(),
                    val HasOtherField: Boolean): Parcelable

@Parcelize
data class SubField(val Label: String?): Parcelable

@Parcelize
data class Choice(val Label: String?): Parcelable