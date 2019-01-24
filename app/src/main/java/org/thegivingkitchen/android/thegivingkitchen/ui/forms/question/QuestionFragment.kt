package org.thegivingkitchen.android.thegivingkitchen.ui.forms.question

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Page
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.*
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.util.*

class QuestionFragment: Fragment(), BackPressedListener, DatePickerDialog.OnDateSetListener {
    private var dateQuestion: DateQuestion? = null
    private var dateYear: Int? = null
    private var dateMonth: Int? = null
    private var dateDay: Int? = null

    companion object {
        const val pageArg = "page"

        fun newInstance(page: Page): QuestionFragment {
            val formQuestionFragment = QuestionFragment()
            val args = Bundle()
            args.putParcelable(pageArg, page)
            formQuestionFragment.arguments = args
            return formQuestionFragment
        }
    }

    lateinit var page: Page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            page = arguments!!.getParcelable(pageArg)!!
        }
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTitle_formQuestion.setTextIfItExists(page.pageInformation)

        /*  Testing code  */
        container_formQuestion.addView(FullnameQuestion("Question 1", context!!))
        container_formQuestion.addView(PhoneQuestion("Question 2", context!!))
        container_formQuestion.addView(EmailQuestion("Question 3", context!!))

        val dateQuestionView = DateQuestion("Question 4", context!!)
        dateQuestion = dateQuestionView
        container_formQuestion.addView(dateQuestion)
        dateQuestion!!.setOnClickListener(dateViewClickListener)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private val dateViewClickListener = View.OnClickListener {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.newInstance(this, dateYear, dateMonth, dateDay)
        datePickerFragment.show(fragmentManager, "hello")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateYear = year
        dateMonth = month
        dateDay = dayOfMonth

        dateQuestion?.setDate(dateMonth!!, dateDay!!, dateYear!!)
    }
}

class DatePickerFragment: DialogFragment() {
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    fun newInstance(listener: DatePickerDialog.OnDateSetListener, selectedYear: Int? = null, selectedMonth: Int? = null, selectedDay: Int? = null) {
        dateSetListener = listener

        val calendar = Calendar.getInstance()
        if (selectedYear != null) {
            year = selectedYear
        } else {
            year = calendar.get(Calendar.YEAR)
        }
        if (selectedMonth != null) {
            month = selectedMonth
        } else {
            month = calendar.get(Calendar.MONTH)
        }
        if (selectedDay != null) {
            day = selectedDay
        } else {
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, dateSetListener, year, month, day)
    }
}