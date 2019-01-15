package org.thegivingkitchen.android.thegivingkitchen.ui.forms.question

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.thegivingkitchen.android.thegivingkitchen.R

class FormQuestionFragment: Fragment() {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val randomNumber = Math.random()
        textView_formQuestion.text = randomNumber.toString()
    }
}