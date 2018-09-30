package org.thegivingkitchen.android.thegivingkitchen.ui.forms

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_forms_prologue_fragment.*
import org.thegivingkitchen.android.thegivingkitchen.R

abstract class BaseFormsPrologueFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.base_forms_prologue_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_baseFormsPrologue.text = getString(titleText())
        subtitle_baseFormsPrologue.text = getString(subtitleText())
        description_baseFormsPrologue.text = getString(descriptionText())

    }

    abstract fun titleText(): Int

    abstract fun subtitleText(): Int

    abstract fun descriptionText(): Int

}