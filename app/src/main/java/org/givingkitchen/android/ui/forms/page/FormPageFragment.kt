package org.givingkitchen.android.ui.forms.page

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.forms.Page
import org.givingkitchen.android.ui.forms.Question
import org.givingkitchen.android.ui.forms.questionviews.*
import org.givingkitchen.android.ui.forms.questionviews.checkboxquestion.CheckboxQuestion
import org.givingkitchen.android.ui.forms.questionviews.datequestion.DatePickerFragment
import org.givingkitchen.android.ui.forms.questionviews.datequestion.DateQuestion
import org.givingkitchen.android.ui.forms.questionviews.moneyquestion.MoneyQuestion
import org.givingkitchen.android.ui.forms.questionviews.radioquestion.RadioQuestion
import org.givingkitchen.android.ui.forms.questionviews.timequestion.TimePickerFragment
import org.givingkitchen.android.ui.forms.questionviews.timequestion.TimeQuestion
import org.givingkitchen.android.util.setTextIfItExists

class FormPageFragment : Fragment() {

    companion object {
        const val pageArg = "page"
        const val formIdArg = "formId"

        fun newInstance(page: Page, formId: String): FormPageFragment {
            val formPageFragment = FormPageFragment()
            val args = Bundle()
            args.putParcelable(pageArg, page)
            args.putString(formIdArg, formId)
            formPageFragment.arguments = args
            return formPageFragment
        }
    }

    private lateinit var page: Page
    private lateinit var formId: String
    val questionViews = mutableListOf<QuestionView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            page = arguments!!.getParcelable(pageArg)!!
            formId = arguments!!.getString(formIdArg)!!
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
                if (questionView != null && questionView is QuestionView) {
                    container_formQuestion.addView(questionView)
                    questionViews.add(questionView)
                } else {
                    Crashlytics.log("Encountered unexpected question type: $question")
                }
            }
        }
    }

    /* fun getQuestionResponses(): List<QuestionResponse> {
        val questionResponses = arrayListOf<QuestionResponse>()

        for (questionView in questionsWithViews) {
            val questionId = questionView.question.ID
            val questionTitle = questionView.question.Title
            val questionAnswer = questionView.questionView.getAnswer()

            if (questionTitle != null && questionAnswer != null) {
                questionResponses.add(QuestionResponse(questionId, questionTitle, questionAnswer))
            }
        }

        return questionResponses
    }*/

    private fun getQuestionView(question: Question): View? {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val savedAnswer = sharedPref?.getString(formId + question.ID, null)

        return when (question.Type) {
            QuestionType.shortname -> {
                ShortnameQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.fullname -> {
                FullnameQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.text -> {
                TextQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.phone -> {
                PhoneQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.email -> {
                EmailQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.address -> {
                AddressQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.date -> {
                val dateQuestion = DateQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
                dateQuestion.setOnClickListener {
                    DatePickerFragment().newInstance(dateQuestion, dateQuestion.dateYear, dateQuestion.dateMonth-1, dateQuestion.dateDay).show(fragmentManager, "Date")
                }
                dateQuestion
            }
            QuestionType.time -> {
                val timeQuestion = TimeQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
                timeQuestion.setOnClickListener {
                    TimePickerFragment().newInstance(timeQuestion, timeQuestion.timeHour, timeQuestion.timeMinute).show(fragmentManager, "Time")
                }
                timeQuestion
            }
            QuestionType.number -> {
                NumberQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.money -> {
                MoneyQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.checkbox -> {
                CheckboxQuestion(question, formatTitle(question.Title, question.IsRequired), question.SubFields?.map { it.Label }, question.HasOtherField, savedAnswer, context!!)
            }
            QuestionType.textarea -> {
                TextareaQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.url -> {
                UrlQuestion(question, formatTitle(question.Title, question.IsRequired), savedAnswer, context!!)
            }
            QuestionType.radio -> {
                RadioQuestion(question, formatTitle(question.Title, question.IsRequired), question.Choices?.map { it.Label }, question.HasOtherField, savedAnswer, context!!)
            }
            QuestionType.select -> {
                RadioQuestion(question, formatTitle(question.Title, question.IsRequired), question.Choices?.map { it.Label }, question.HasOtherField, savedAnswer, context!!)
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

class QuestionResponse(val id: String, val question: String, val answer: String)
