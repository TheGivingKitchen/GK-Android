package org.givingkitchen.android.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import org.givingkitchen.android.R

class VerticalShadowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): FrameLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_vertical_shadow, this, true)
    }
}