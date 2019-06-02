package org.givingkitchen.android.ui.forms

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.givingkitchen.android.ui.forms.page.QuestionType

@Parcelize
class Form(val ID: String,
           val FormTitle: String?,
           val FormSubtitle: String?,
           val FormMetadata: String?,
           val FormShareString: String?,
           val Pages: List<Page> = listOf()): Parcelable

@Parcelize
data class Page(val pageInformation: String?,
                val questions: List<Question>? = listOf()): Parcelable

@Parcelize
data class Question(val Title: String?,
                    val IsRequired: String?,
                    val Type: QuestionType?,
                    val SubFields: List<SubField>? = listOf(),
                    val Choices: List<Choice>? = listOf(),
                    val ID: String,
                    val HasOtherField: Boolean,
                    var answer: String? = null,
                    var warning: String? = null): Parcelable

@Parcelize
data class SubField(val Label: String?): Parcelable

@Parcelize
data class Choice(val Label: String?): Parcelable