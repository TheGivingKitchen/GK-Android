package org.givingkitchen.android.ui.homescreen.resources

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class ResourceProvidersList(
        val records: List<ResourceProviderContainer>
)

@Parcelize
data class ResourceProviderContainer(
        val id: String,
        val fields: ResourceProvider
): Parcelable

@Parcelize
data class ResourceProvider(
        val name: String,
        val address1: String?,
        val address2: String?,
        val website: String?,
        val phone: String?,
        val contactName: String?,
        val category: String?,
        val description: String?,
        val countiesServed: List<String>?,
        val latitude: Double?,
        val longitude: Double?
): Parcelable