package org.thegivingkitchen.android.thegivingkitchen.util

interface BackPressedListener {
    /**
     * @return true if this fragment consumes the back pressed event, if not then false
     */
    fun onBackPressed(): Boolean
}