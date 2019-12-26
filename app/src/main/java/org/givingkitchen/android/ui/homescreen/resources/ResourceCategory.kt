package org.givingkitchen.android.ui.homescreen.resources

class ResourceCategory(val title: String) {
    companion object {
        val resourceCategories = listOf(
                ResourceCategory("Physical Health and Wellness"),
                ResourceCategory("Mental Health"),
                ResourceCategory("Family and Social Services"),
                ResourceCategory("Housing/Utilities"),
                ResourceCategory("Financial Services"),
                ResourceCategory("Dental/Vision"),
                ResourceCategory("Employment"),
                ResourceCategory("Women's/LGBTQ"),
                ResourceCategory("Substance Abuse and Addiction")
        )
    }
}