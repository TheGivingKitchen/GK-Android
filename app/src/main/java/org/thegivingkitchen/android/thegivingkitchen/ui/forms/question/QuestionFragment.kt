package org.thegivingkitchen.android.thegivingkitchen.ui.forms.question

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.annotation.Nullable
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
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.checkboxquestion.CheckboxQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion.DatePickerFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion.DateQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.moneyquestion.MoneyQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.radioquestion.RadioQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion.TimePickerFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion.TimeQuestion
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

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

    private lateinit var page: Page

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
        container_formQuestion.addView(ShortnameQuestion("Shortname Question", context!!))
        container_formQuestion.addView(PhoneQuestion("Phone Question", context!!))
        container_formQuestion.addView(EmailQuestion("Email Question", context!!))

        val dateQuestionView = DateQuestion("Date Question", context!!)
        dateQuestion = dateQuestionView
        container_formQuestion.addView(dateQuestion)
        dateQuestion!!.setOnClickListener(dateViewClickListener)

        val timeQuestionView = TimeQuestion("Time Question", context!!)
        timeQuestion = timeQuestionView
        container_formQuestion.addView(timeQuestion)
        timeQuestion!!.setOnClickListener(timeViewClickListener)

        container_formQuestion.addView(NumberQuestion("Number Question", context!!))
        container_formQuestion.addView(TextQuestion("Text Question", context!!))
        container_formQuestion.addView(UrlQuestion("Url Question", context!!))
        container_formQuestion.addView(TextareaQuestion("Textarea Question", context!!))
        container_formQuestion.addView(CheckboxQuestion("Checkbox Question", listOf("hi", "hello", "namaste", "whatsup", "hola"), true, context!!))
        container_formQuestion.addView(RadioQuestion("Radio Question", listOf("bye", "goodbye", "alvida", "see ya later", "adiós"), true, context!!))
        container_formQuestion.addView(FullnameQuestion("Fullname Question", context!!))
        container_formQuestion.addView(MoneyQuestion("Money Question", context!!))
        container_formQuestion.addView(AddressQuestion("Address Question", context!!))

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

        var timePeriod = getString(R.string.time_question_am)
        var formattedHour = timeHour!!
        if (timeHour!! >= 12) {
            timePeriod = getString(R.string.time_question_pm)
            formattedHour-=12
        }
        if (formattedHour == 0) {
            formattedHour = 12
        }
        timeQuestion?.setTime(formattedHour, timeMinute!!, timePeriod)
    }
}
