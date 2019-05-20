package org.givingkitchen.android.ui.forms

class WufooResponse(val Success: Int,
                    val FieldErrors: List<FieldError>? = listOf())

class FieldError(val ID: String, val ErrorText: String)