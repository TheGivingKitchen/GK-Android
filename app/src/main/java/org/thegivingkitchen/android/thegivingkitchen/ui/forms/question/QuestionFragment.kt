package org.thegivingkitchen.android.thegivingkitchen.ui.forms.question

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_form_question.*
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.Page
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.DateQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.EmailQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.FullnameQuestion
import org.thegivingkitchen.android.thegivingkitchen.ui.forms.questionviews.PhoneQuestion
import org.thegivingkitchen.android.thegivingkitchen.util.BackPressedListener
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class QuestionFragment: Fragment(), BackPressedListener {
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
        container_formQuestion.addView(DateQuestion("Question 4", fragmentManager!!, context!!))
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}