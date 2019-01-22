package org.thegivingkitchen.android.thegivingkitchen.util

import android.content.Intent
import android.content.res.Resources
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.View
import android.widget.TextView

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

/**
 * @return true if the text was set or false if the TextView is now Gone
 */
fun TextView.setTextIfItExists(text: String?): Boolean {
    // todo: it you scroll really fast(?) the fields sometimes don't show up
    if (text.isNullOrBlank()) {
        this.visibility = View.GONE
        return false
    } else {
        this.text = text
        return true
    }
}