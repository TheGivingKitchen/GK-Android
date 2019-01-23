package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Form(val ID: String?,
           val FormTitle: String?,
           val FormSubtitle: String?,
           val FormMetadata: String?,
           val FormShareString: String?,
           val Pages: List<Page> = listOf())

@Parcelize
data class Page(val pageInformation: String?,
                val questions: List<Question> = listOf()): Parcelable

@Parcelize
data class Question(val Title: String?,
                    val IsRequired: String?,
                    val Type: String?): Parcelable

