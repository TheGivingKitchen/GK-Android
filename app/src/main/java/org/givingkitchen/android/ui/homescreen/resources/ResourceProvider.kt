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

class ResourceCategory(val title: String) {
    companion object {
        val resourceCategories = listOf(
                ResourceCategory("Homeless Prevention"),
                ResourceCategory("Employee Assistance"),
                ResourceCategory("Educational Assistance"),
                ResourceCategory("Elderly Assistance (In-home Respite Care)"),
                ResourceCategory("Pharmacy"),
                ResourceCategory("Family Assistance"),
                ResourceCategory("Transitional Employment Services"),
                ResourceCategory("HIV/AIDS"),
                ResourceCategory("Entrepreneurship Assistance"),
                ResourceCategory("Employment Services"),
                ResourceCategory("Elderly Assistance (Housing)"),
                ResourceCategory("Legal Services"),
                ResourceCategory("Homeless Assistance"),
                ResourceCategory("Peer Support Services"),
                ResourceCategory("Financial Assistance"),
                ResourceCategory("Housing & Ecoonomic Services"),
                ResourceCategory("Elderly Assistance (Transportation)"),
                ResourceCategory("Health and Wellness"),
                ResourceCategory("Mobility Services"),
                ResourceCategory("Dental"),
                ResourceCategory("Substance Abuse"),
                ResourceCategory("Developmental Disabilities"),
                ResourceCategory("Disabled Assistance (Housing)"),
                ResourceCategory("Medical/Prescriptions"),
                ResourceCategory("Funeral Services"),
                ResourceCategory("Utility Assistance"),
                ResourceCategory("Domestic Violence"),
                ResourceCategory("Financial Services"),
                ResourceCategory("Crisis Assistance"),
                ResourceCategory("Information/Resource Services"),
                ResourceCategory("Transportation Assistance"),
                ResourceCategory("Women's Services"),
                ResourceCategory("Mental Health"),
                ResourceCategory("Business Assistance"),
                ResourceCategory("Financial Services, Medical, Food Pantry"),
                ResourceCategory("Information/ Referral Services"),
                ResourceCategory("Career/ Life Empowerment Services"),
                ResourceCategory("Healthcare"),
                ResourceCategory("Transition Services"),
                ResourceCategory("Tool Lending Program"),
                ResourceCategory("Domestic Violence, Counseling, Therapy"),
                ResourceCategory("Elderly Assistance"),
                ResourceCategory("Emergency Assistance"),
                ResourceCategory("Financial Literacy"),
                ResourceCategory("Food Assistance"),
                ResourceCategory("Cancer"),
                ResourceCategory("Elderly Assistance (Homemaking)"),
                ResourceCategory("Elderly Assistance (Meals)"),
                ResourceCategory("Family & Children Housing"),
                ResourceCategory("Peer Counseling Services"),
                ResourceCategory("Children & Family"),
                ResourceCategory("Immigration Assistance"),
                ResourceCategory("Healthcare (Insurance)"),
                ResourceCategory("Independent Living Services"),
                ResourceCategory("Housing Assistance"),
                ResourceCategory("Community Outreach"),
                ResourceCategory("Advocacy Services")
        )
    }
}