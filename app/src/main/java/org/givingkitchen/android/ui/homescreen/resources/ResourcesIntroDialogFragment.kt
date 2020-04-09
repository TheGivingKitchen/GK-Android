package org.givingkitchen.android.ui.homescreen.resources

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_resources_intro_dialog.*
import org.givingkitchen.android.R

class ResourcesIntroDialogFragment: DialogFragment() {
    private val dialogDismissed: PublishSubject<Boolean> = PublishSubject.create()

    fun dialogDismissed(): Observable<Boolean> = dialogDismissed

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resources_intro_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_resourcesIntroDialog.setOnClickListener { dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogDismissed.onNext(false)
    }
}