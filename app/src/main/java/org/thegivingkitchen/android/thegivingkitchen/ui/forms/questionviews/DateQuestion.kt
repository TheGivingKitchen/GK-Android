package org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews

import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.view_question_date.view.*
import org.thegivingkitchen.android.thegivingkitchen.R

// todo: stop passing in fragment manager here
class DateQuestion(title: String?, fragmentManager: FragmentManager, context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle), ActionHandler {
    // todo: use merge tags in views
    private val textViewClickListener = View.OnClickListener {
        DatePickerFragment().show(fragmentManager, "hello")
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_question_date, this, true)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        title_dateQuestion.text = title
        setOnClickListener(textViewClickListener)
    }

    override fun handleAction(actionCode: String, result: String) {
        when {
            actionCode == DatePickerFragment.FRAGMENT_A_CLOSED -> {
                date_dateQuestion.text = result
            }
        }
    }
}

// todo: make this available to apis < 25
@TargetApi(25)
class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {
    companion object {
        const val FRAGMENT_A_CLOSED = "com.example.fragment_a_closed"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // date_dateQuestion.text = context!!.getString(R.string.date_question_date, dayOfMonth, month, year)
        closeFragment(context!!.getString(R.string.date_question_date, dayOfMonth, month, year))
    }

    fun closeFragment(str: String) {
        try {
            (activity as ActionHandler).handleAction(FRAGMENT_A_CLOSED, str)
        } catch (e: ClassCastException) {
            // Timber.e("Calling activity can't get callback!")
        }
        dismiss()
    }
}

interface ActionHandler {
    fun handleAction(actionCode: String, result: String)
}

