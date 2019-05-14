package org.givingkitchen.android.ui.homescreen.give

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_give.*
import org.givingkitchen.android.R
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.creditCardDonationURL
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.giveLearnMoreURL
import org.givingkitchen.android.ui.homescreen.give.GiveViewModel.Companion.recurringDonationURL
import org.givingkitchen.android.util.Constants
import org.givingkitchen.android.util.CustomTabs
import org.givingkitchen.android.util.DonePage
import org.givingkitchen.android.util.putEnum

class GiveFragment : Fragment() {
    private lateinit var model: GiveViewModel
    private lateinit var timerHandler: Handler
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(GiveViewModel::class.java)
        model.getDonationExample().observe(this, Observer<GiveViewModel.DonationExample> { liveData ->
            updateDonationExample(liveData)
        })
    }

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_give, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        learn_more_button_giveTab.setOnClickListener(learnMoreButtonClickListener)
        card_donate_button_giveTab.setOnClickListener(creditCardDonationClickListener)
        recurring_donate_button_giveTab.setOnClickListener(recurringDonationClickListener)
        volunteer_button_giveTab.setOnClickListener(volunteerButtonClickListener)
        partner_button_giveTab.setOnClickListener(partnerButtonClickListener)
        joinOurForcesButton_giveTab.setOnClickListener(joinForcesButtonClickListener)
        timerHandler = Handler()
        timerRunnable = object : Runnable {
            override fun run() {
                model.nextDonationExample()
                timerHandler.postDelayed(this, 4000)
            }
        }
        timerHandler.postDelayed(timerRunnable, 0)
    }

    override fun onPause() {
        super.onPause()
        timerHandler.removeCallbacks(timerRunnable)
    }

    private val creditCardDonationClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, creditCardDonationURL)
    }

    private val recurringDonationClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, recurringDonationURL)
    }

    private val learnMoreButtonClickListener = View.OnClickListener {
        CustomTabs.openCustomTab(context, giveLearnMoreURL)
    }

    private val volunteerButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.volunteerSignupUrl)
        args.putEnum(Constants.donePageArg, DonePage.VOLUNTEER)
        Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
    }

    private val joinForcesButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.joinOurForcesUrl)
        args.putEnum(Constants.donePageArg, DonePage.STABILITY_NETWORK)
        Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
    }

    private val partnerButtonClickListener = View.OnClickListener {
        val args = Bundle()
        args.putString(Constants.formsArg, GiveViewModel.stabilityNetworkPartnerUrl)
        args.putEnum(Constants.donePageArg, DonePage.STABILITY_NETWORK)
        Navigation.findNavController(view!!).navigate(R.id.formsFragment, args)
    }

    private fun updateDonationExample(donationExample: GiveViewModel.DonationExample) {

        val amountAnimationListener = object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                carouselTextAmount_giveTab.text = donationExample.amount
                carouselTextAmount_giveTab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_from_right))
            }
        }
        val amountAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right)
        amountAnimation.setAnimationListener(amountAnimationListener)
        carouselTextAmount_giveTab.startAnimation(amountAnimation)

        val descriptionAnimationListener = object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                carouselTextDescription_giveTab.text = getString(donationExample.description)
                carouselTextDescription_giveTab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_from_right))
            }
        }
        val descriptionAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right)
        descriptionAnimation.setAnimationListener(descriptionAnimationListener)
        carouselTextDescription_giveTab.startAnimation(descriptionAnimation)

        val iconAnimationListener = object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) { }
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) {
                carouselIcon_giveTab.setImageDrawable(resources.getDrawable(donationExample.drawable, context!!.theme))
                carouselIcon_giveTab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_from_left))
            }
        }
        val iconAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_to_left)
        iconAnimation.setAnimationListener(iconAnimationListener)
        carouselIcon_giveTab.startAnimation(iconAnimation)
    }
}