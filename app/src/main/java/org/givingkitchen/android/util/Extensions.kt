package org.givingkitchen.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import org.givingkitchen.android.R
import java.util.*

fun Resources.getFloatDimension(@DimenRes dimension: Int): Float {
    val outValue = TypedValue()
    this.getValue(dimension, outValue, true)
    return outValue.float
}

val Resources.currentLocale: Locale
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            configuration.locale
        }
    }

fun Fragment.startShareAction(str: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, str)
        type = "text/plain"
    }
    startActivity(sendIntent)
}

fun View.setPaddingDp(leftDp: Int, topDp: Int, rightDp: Int, bottomDp: Int) {
    this.setPadding(convertToDp(leftDp, resources), convertToDp(topDp, resources), convertToDp(rightDp, resources), convertToDp(bottomDp, resources))
}

fun convertToDp(sizeInDp: Int, resources: Resources): Int {
    val scale = resources.displayMetrics.density
    return (sizeInDp * scale + 0.5f).toInt()
}

fun Bundle.putEnum(key: String, enum: Enum<*>) {
    putString(key, enum.name)
}

inline fun <reified T : Enum<T>> Bundle.getEnum(key: String, default: T): T {
    val str = getString(key) ?: return default
    return try {
        enumValueOf(str)
    } catch (e: Exception) {
        default
    }
}

fun SearchView.hasQuery(): Boolean {
    return this.query.isNotEmpty()
}

fun Activity?.getGivingKitchenSharedPreferences(): SharedPreferences? {
    return this?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
}

fun Activity?.hideKeyboard() {
    val view = this?.currentFocus
    view?.let {
        val inputMethodManager = it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * @return true if the text was set or false if the TextView is now Gone
 */
fun TextView.setTextIfItExists(text: String?): Boolean {
    return if (text.isNullOrBlank()) {
        this.visibility = View.GONE
        false
    } else {
        this.visibility = View.VISIBLE
        this.text = text
        true
    }
}