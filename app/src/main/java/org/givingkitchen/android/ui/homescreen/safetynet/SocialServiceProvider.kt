package org.givingkitchen.android.ui.homescreen.safetynet

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class SocialServiceProvidersList(
        val safetyNet: List<SocialServiceProvider>
)

@Parcelize
data class SocialServiceProvider(
        var index: Int?,
        val name: String,
        val address: String?,
        val website: String?,
        val phone: String?,
        val contactName: String?,
        val category: String?,
        val description: String?,
        val countiesServed: String?
): Parcelable

class Header
