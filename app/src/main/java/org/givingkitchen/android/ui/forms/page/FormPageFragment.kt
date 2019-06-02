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

    private fun getQuestionView(question: Question): View? {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val savedAnswer = sharedPref?.getString(formId + question.ID, null)

        return when (question.Type) {
            QuestionType.shortname -> {
                ShortnameQuestion(question, context!!)
            }
            QuestionType.fullname -> {
                FullnameQuestion(question, context!!)
            }
            QuestionType.text -> {
                TextQuestion(question, context!!)
            }
            QuestionType.phone -> {
                PhoneQuestion(question, context!!)
            }
            QuestionType.email -> {
                EmailQuestion(question, context!!)
            }
            QuestionType.address -> {
                AddressQuestion(question, context!!)
            }
            QuestionType.date -> {
                val dateQuestion = DateQuestion(question, context!!)
                dateQuestion.setOnClickListener {
                    val month = if (dateQuestion.dateMonth != null) {
                        dateQuestion.dateMonth!!-1
                    } else {
                        null
                    }
                    DatePickerFragment().newInstance(dateQuestion, dateQuestion.dateYear, month, dateQuestion.dateDay).show(fragmentManager, "Date")
                }
                dateQuestion
            }
            QuestionType.time -> {
                val timeQuestion = TimeQuestion(question, context!!)
                timeQuestion.setOnClickListener {
                    TimePickerFragment().newInstance(timeQuestion, timeQuestion.timeHour, timeQuestion.timeMinute).show(fragmentManager, "Time")
                }
                timeQuestion
            }
            QuestionType.number -> {
                NumberQuestion(question, context!!)
            }
            QuestionType.money -> {
                MoneyQuestion(question, context!!)
            }
            QuestionType.checkbox -> {
                CheckboxQuestion(question, context!!)
            }
            QuestionType.textarea -> {
                TextareaQuestion(question, context!!)
            }
            QuestionType.url -> {
                UrlQuestion(question, context!!)
            }
            QuestionType.radio -> {
                RadioQuestion(question, context!!)
            }
            QuestionType.select -> {
                RadioQuestion(question, context!!)
            }
            else -> {
                null
            }
        }
    }
}
