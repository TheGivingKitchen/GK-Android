package org.givingkitchen.android.ui.homescreen.resources

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class ResourceProvidersList(
        val safetyNet: List<ResourceProvider>
)

@Parcelize
data class ResourceProvider(
        var index: Int?,
        val name: String,
        val address: String?,
        val website: String?,
        val phone: String?,
        val contactName: String?,
        val category: String?,
        val description: String?,
        val countiesServed: String?,
        val latitude: Double?,
        val longitude: Double?
): Parcelable