package org.thegivingkitchen.android.thegivingkitchen.ui.homescreen.safetynet.safetynettab

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SocialServiceProvidersList(
        val safetyNet: List<SocialServiceProvider>
)

data class SocialServiceProvider(
        var index: Int?,
        val name: String?,
        val address: String?,
        val website: String?,
        val phone: String?,
        val contactName: String?,
        val category: String?,
        val description: String?,
        val countiesServed: String?
)

class Header
