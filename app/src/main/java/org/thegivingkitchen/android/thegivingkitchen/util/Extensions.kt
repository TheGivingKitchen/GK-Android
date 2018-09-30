package org.thegivingkitchen.android.thegivingkitchen.util

import android.content.res.Resources
import android.support.annotation.DimenRes
import android.util.TypedValue

fun Resources.getFloatDimension(@DimenRes dimension: Int): Float {
    val outValue = TypedValue()
    this.getValue(dimension, outValue, true)
    return outValue.float
}