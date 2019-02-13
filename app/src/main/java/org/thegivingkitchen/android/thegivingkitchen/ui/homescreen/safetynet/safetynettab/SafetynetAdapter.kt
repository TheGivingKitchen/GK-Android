package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.safetynettab

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.thegivingkitchen.android.thegivingkitchen.R
import org.thegivingkitchen.android.thegivingkitchen.util.setTextIfItExists

class SafetynetAdapter(var items: MutableList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val learnMoreClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val joinUsClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val resourcesFilterClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val countyFilterClicks: PublishSubject<Boolean> = PublishSubject.create()
    private val serviceProviderClicks: PublishSubject<Int> = PublishSubject.create()

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SERVICE_PROVIDER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                VIEW_TYPE_HEADER
            }
            else -> {
                VIEW_TYPE_SERVICE_PROVIDER
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_safetynet_header, parent, false), learnMoreClicks, joinUsClicks, resourcesFilterClicks, countyFilterClicks)
            }
            else -> {
                SocialServiceProviderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_social_service_provider, parent, false), serviceProviderClicks)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SocialServiceProviderViewHolder) {
            holder.bind(items[position] as SocialServiceProvider)
        } else {
            (holder as HeaderViewHolder).bind()
        }
    }

    override fun getItemCount() = items.size

    fun learnMoreClicks(): Observable<Boolean> = learnMoreClicks
    fun joinUsClicks(): Observable<Boolean> = joinUsClicks
    fun resourcesFilterClicks(): Observable<Boolean> = resourcesFilterClicks
    fun countiesFilterClicks(): Observable<Boolean> = countyFilterClicks
    fun serviceProviderClicks(): Observable<Int> = serviceProviderClicks
}

class SocialServiceProviderViewHolder(val view: View, private val clicks: PublishSubject<Int>) : RecyclerView.ViewHolder(view) {
    fun bind(socialServiceProvider: SocialServiceProvider) {
        view.findViewById<TextView>(R.id.title_SafetynetRecycler).setTextIfItExists(socialServiceProvider.name)
        view.findViewById<TextView>(R.id.category_SafetynetRecycler).setTextIfItExists(socialServiceProvider.category)
        view.findViewById<TextView>(R.id.address_SafetynetRecycler).setTextIfItExists(socialServiceProvider.address)
        view.findViewById<TextView>(R.id.phone_SafetynetRecycler).setTextIfItExists(socialServiceProvider.phone)
        view.findViewById<TextView>(R.id.description_SafetynetRecycler).setTextIfItExists(socialServiceProvider.description)
        val cellIndex = socialServiceProvider.index
        if (cellIndex != null) {
            view.setOnClickListener { clicks.onNext(cellIndex) }
        }

        val countiesServedText = socialServiceProvider.countiesServed
        val countiesServedTextView = view.findViewById<TextView>(R.id.counties_SafetynetRecycler)
        if (countiesServedText.isNullOrBlank()) {
            countiesServedTextView.visibility = View.GONE
        } else {
            countiesServedTextView.text = view.context.getString(R.string.safetynet_tab_counties_served, countiesServedText)
        }
    }
}

class HeaderViewHolder(val view: View, private val learnMoreClicks: PublishSubject<Boolean>, private val joinUsClicks: PublishSubject<Boolean>, private val resourcesFilterClicks: PublishSubject<Boolean>, private val countyFilterClicks: PublishSubject<Boolean>) : RecyclerView.ViewHolder(view) {
    fun bind() {
        view.findViewById<TextView>(R.id.learn_more_button_safetynetTab).setOnClickListener { learnMoreClicks.onNext(false) }
        view.findViewById<TextView>(R.id.join_us_button_safetynetTab).setOnClickListener { joinUsClicks.onNext(false) }
        view.findViewById<LinearLayout>(R.id.resourcesFilter_safetynetTab).setOnClickListener { resourcesFilterClicks.onNext(false) }
        view.findViewById<LinearLayout>(R.id.countiesFilter_safetynetTab).setOnClickListener { countyFilterClicks.onNext(false) }
    }
}
