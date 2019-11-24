package org.givingkitchen.android.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.view_banner.view.*
import org.givingkitchen.android.R
import rx.Observable

class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_banner, this, true)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        this.orientation = HORIZONTAL
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.gk_blue))

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.BannerView, 0, 0)
            title_bannerView.text = typedArray.getString(R.styleable.BannerView_text_BannerView)
            typedArray.recycle()
        }
    }

    fun onTitleClick(): Observable<Void> {
        return RxView.clicks(title_bannerView)
    }

    fun onCloseClick(): Observable<Void> {
        return RxView.clicks(closeButton_bannerView)
    }
}