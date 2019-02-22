package org.givingkitchen.android.ui.homescreen.about.feedback

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feedback.*
import org.givingkitchen.android.R

class FeedbackFragment: Fragment()  {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upButton_feedbackScreen.setOnClickListener(upButtonClickListener)
        submit_feedbackScreen.setOnClickListener(submitButtonClickListener)
    }

    private val upButtonClickListener = View.OnClickListener {
        // todo: put an onBackPressed() call here
    }

    private val submitButtonClickListener = View.OnClickListener {
        // todo: actually submit the feedback
        val a = SubmitFeedbackDialogFragment()
        a.show(fragmentManager, "Feedback_Submit_Success")
    }
}