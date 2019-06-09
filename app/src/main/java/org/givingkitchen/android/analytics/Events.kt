package org.givingkitchen.android.analytics

enum class Events(name: String){
    EVENT_VIEW_DETAILS("event_home_view_event"),
    EVENT_VOLUNTEER_BANNER_CLOSE("event_volunteer_banner_close"), //add when exist
    EVENT_VOLUNTEER_BANNER_OPEN("event_volunteer_banner_open"), //add when exist

    DONATE_ONE_TIME_DONATION_STARTED("donation_one_time_started"),
    DONATE_RECURRING_DONATION_STARTED("donation_recurring_started"),

    FORM_VIEW_DETAILS("form_view_details"),
    FORM_STARTED("form_started"),
    FORM_COMPLETED("form_completed"),

    SAFETY_NET_LOCATION_SEARCH("safetynet_change_to_location_search"), //add when exist
    SAFETY_NET_GLOBAL_SEARCH("safetynet_change_to_global_search"), //add when exist
    SAFETY_NET_VISIT_WEBSITE("safetynet_visit_website"),
    SAFETY_NET_VISIT_ADDRESS("safetynet_visit_address"),
    SAFETY_NET_CALL_PHONE("safetynet_call_phone"),
    SAFETY_NET_SEARCH("safetynet_search"),
    SAFETY_NET_FACEBOOK_GROUP_VISIT("safetynet_visit_facebook_group"),

    NEWSLETTER_SIGNUP_STARTED("newsletter_signup_started"),
    FEEDBACK_POSITIVE("feedback_positive"),
    FEEDBACK_COMMENT("feedback_comment"),
    FEEDBACK_REPORT_PROBLEM("feedback_report_problem"),

    LEARN_MORE_PRESSED("learn_more_pressed"),
}

enum class Parameter(name: String) {
    EVENT_VIEW_DETAILS_EVENT_NAME("event_name"),
    EVENT_VIEW_DETAILS_EVENT_URL("event_url"),

    FORM_NAME("form_name"),
    FORM_ID("form_id"),

    SAFETY_NET_NAME("safetynet_name"),
    SAFETY_NET_SEARCH_TERM("search_term"),
    SAFETY_NET_SEARCH_LOCATION_BASED("is_location_based"),
    SAFETY_NET_FACEBOOK_GROUP_NAME("facebook_group_name"),

    LEARN_MORE_TYPE("learn_more_type"),
}