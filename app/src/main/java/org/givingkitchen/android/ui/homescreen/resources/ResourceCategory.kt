package org.givingkitchen.android.ui.homescreen.resources

import org.givingkitchen.android.R

class ResourceCategory(val title: String, val icon: Int) {
    companion object {
        val resourceCategories = listOf(
                ResourceCategory("Physical Health and Wellness", R.drawable.ic_default_resource),
                ResourceCategory("Mental Health", R.drawable.ic_mental_health_resource),
                ResourceCategory("Family and Social Services", R.drawable.ic_family_resource),
                ResourceCategory("Housing/Utilities", R.drawable.ic_housing_resource),
                ResourceCategory("Financial Services", R.drawable.ic_financial_resource),
                ResourceCategory("Dental/Vision", R.drawable.ic_dental_resource),
                ResourceCategory("Employment", R.drawable.ic_employment_resource),
                ResourceCategory("Women's/LGBTQ", R.drawable.ic_womens_resource),
                ResourceCategory("Substance Abuse and Addiction", R.drawable.ic_addiction_resource)
        )
    }
}