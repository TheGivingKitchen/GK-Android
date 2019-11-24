package org.givingkitchen.android.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import org.givingkitchen.android.R

fun Resources.getFloatDimension(@DimenRes dimension: Int): Float {
    val outValue = TypedValue()
    this.getValue(dimension, outValue, true)
    return outValue.float
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

fun Bundle.putEnum(key:String, enum: Enum<*>){
    putString(key, enum.name)
}

inline fun <reified T: Enum<T>> Bundle.getEnum(key:String, default:T): T {
    return try {
        enumValueOf(getString(key))
    } catch (e: IllegalStateException) {
        default
    }
}

fun Activity?.getGivingKitchenSharedPreferences(): SharedPreferences? {
    return this?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
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