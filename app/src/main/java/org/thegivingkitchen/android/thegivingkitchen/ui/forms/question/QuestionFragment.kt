package org.thegivingkitchen.android.thegivingkitchen.ui.forms.question

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Page
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.*
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists
import java.util.*

class QuestionFragment: Fragment(), BackPressedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var dateQuestion: DateQuestion? = null
    private var dateYear: Int? = null
    private var dateMonth: Int? = null
    private var dateDay: Int? = null

    private var timeQuestion: TimeQuestion? = null
    private var timeHour: Int? = null
    private var timeMinute: Int? = null

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
        container_formQuestion.addView(ShortnameQuestion("Question 1", context!!))
        container_formQuestion.addView(PhoneQuestion("Question 2", context!!))
        container_formQuestion.addView(EmailQuestion("Question 3", context!!))

        val dateQuestionView = DateQuestion("Question 4", context!!)
        dateQuestion = dateQuestionView
        container_formQuestion.addView(dateQuestion)
        dateQuestion!!.setOnClickListener(dateViewClickListener)

        val timeQuestionView = TimeQuestion("Question 5", context!!)
        timeQuestion = timeQuestionView
        container_formQuestion.addView(timeQuestion)
        timeQuestion!!.setOnClickListener(timeViewClickListener)

        container_formQuestion.addView(NumberQuestion("Question 6", context!!))
        container_formQuestion.addView(TextQuestion("Question 7", context!!))
        container_formQuestion.addView(UrlQuestion("Question 8", context!!))
        container_formQuestion.addView(TextareaQuestion("Question 9", context!!))
        container_formQuestion.addView(CheckboxQuestion("Question 10", listOf("hi", "hello", "namaste", "whatsup", "hola"), true, context!!))
        container_formQuestion.addView(RadioQuestion("Question 11", listOf("bye", "goodbye", "alvida", "see ya later", "adiós"), true, context!!))
        container_formQuestion.addView(FullnameQuestion("Question 12", context!!))
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    private val dateViewClickListener = View.OnClickListener {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.newInstance(this, dateYear, dateMonth, dateDay)
        datePickerFragment.show(fragmentManager, "Date")
    }

    private val timeViewClickListener = View.OnClickListener {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.newInstance(this, timeHour, timeMinute)
        timePickerFragment.show(fragmentManager, "Time")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        dateYear = year
        dateMonth = month
        dateDay = dayOfMonth

        dateQuestion?.setDate(dateMonth!!, dateDay!!, dateYear!!)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        timeHour = hourOfDay
        timeMinute = minute

        // todo: use string resources for am and pm
        var timePeriod = "am"
        var formattedHour = timeHour!!
        if (timeHour!! >= 12) {
            timePeriod = "pm"
            formattedHour-=12
        }
        if (formattedHour == 0) {
            formattedHour = 12
        }
        timeQuestion?.setTime(formattedHour, timeMinute!!, timePeriod)
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

class TimePickerFragment: DialogFragment() {
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private var hour: Int = 0
    private var minute: Int = 0

    fun newInstance(listener: TimePickerDialog.OnTimeSetListener, selectedHour: Int? = null, selectedMinute: Int? = null) {
        timeSetListener = listener

        if (selectedHour != null) {
            hour = selectedHour
        } else {
            hour = 11
        }
        if (selectedMinute != null) {
            minute = selectedMinute
        } else {
            minute = 0
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(context!!, timeSetListener, hour, minute, false)
    }
}