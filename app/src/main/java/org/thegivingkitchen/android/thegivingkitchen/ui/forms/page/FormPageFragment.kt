package org.thegivingkitchen.android.thegivingkitchen.ui.forms.page

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Page
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Question
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.*
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.checkboxquestion.CheckboxQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion.DatePickerFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.datequestion.DateQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.moneyquestion.MoneyQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.radioquestion.RadioQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion.TimePickerFragment
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.timequestion.TimeQuestion
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class FormPageFragment : Fragment() {

    companion object {
        const val pageArg = "page"

        fun newInstance(page: Page): FormPageFragment {
            val formPageFragment = FormPageFragment()
            val args = Bundle()
            args.putParcelable(pageArg, page)
            formPageFragment.arguments = args
            return formPageFragment
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

        val questions = page.questions
        if (questions != null) {
            for (question in questions) {
                val questionView = getQuestionView(question)
                if (questionView != null) {
                    container_formQuestion.addView(questionView)
                } else {
                    // todo: log unexpected question type crash
                }
            }
        }
    }

    private fun getQuestionView(question: Question): View? {
        return when (question.Type) {
            QuestionType.shortname -> {
                ShortnameQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.fullname -> {
                FullnameQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.text -> {
                TextQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.phone -> {
                PhoneQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.email -> {
                EmailQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.address -> {
                AddressQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.date -> {
                val dateQuestion = DateQuestion(formatTitle(question.Title, question.IsRequired), context!!)
                dateQuestion.setOnClickListener {
                    DatePickerFragment().newInstance(dateQuestion, dateQuestion.dateYear, dateQuestion.dateMonth, dateQuestion.dateDay).show(fragmentManager, "Date")
                }
                dateQuestion
            }
            QuestionType.time -> {
                val timeQuestion = TimeQuestion(formatTitle(question.Title, question.IsRequired), context!!)
                timeQuestion.setOnClickListener {
                    TimePickerFragment().newInstance(timeQuestion, timeQuestion.timeHour, timeQuestion.timeMinute).show(fragmentManager, "Time")
                }
                timeQuestion
            }
            QuestionType.number -> {
                NumberQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.money -> {
                MoneyQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.checkbox -> {
                CheckboxQuestion(formatTitle(question.Title, question.IsRequired), question.SubFields?.map { it.Label }, question.HasOtherField, context!!)
            }
            QuestionType.textarea -> {
                TextareaQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.url -> {
                UrlQuestion(formatTitle(question.Title, question.IsRequired), context!!)
            }
            QuestionType.radio -> {
                RadioQuestion(formatTitle(question.Title, question.IsRequired), question.Choices?.map { it.Label }, question.HasOtherField, context!!)
            }
            QuestionType.select -> {
                RadioQuestion(formatTitle(question.Title, question.IsRequired), question.Choices?.map { it.Label }, question.HasOtherField, context!!)
            }
            else -> {
                null
            }
        }
    }

    private fun formatTitle(title: String?, isRequired: String?): String? {
        return if (title != null && isRequired != null && isRequired == "1") "$title*" else title
    }
}
