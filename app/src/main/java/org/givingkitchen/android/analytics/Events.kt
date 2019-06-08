package org.givingkitchen.android.analytics

enum class Events(name: String){
    EVENT_VIEW_DETAILS("event_home_view_event"),
    EVENT_VOLUNTEER_BANNER_CLOSE("event_volunteer_banner_close"),
    EVENT_VOLUNTEER_BANNER_OPEN("event_volunteer_banner_open"),
    DONATE_ONE_TIME_DONATION_STARTED("donation_one_time_started"),
    DONATE_RECURRING_DONATION_STARTED("donation_recurring_started")
}

enum class Parameter(name: String) {
    EVENT_VIEW_DETAILS_EVENT_NAME("event_name"),
    EVENT_VIEW_DETAILS_EVENT_URL("event_url")
}

    /*
        case eventViewDetails = "event_home_view_event"
        case eventVolunteerBannerClose = "event_volunteer_banner_close"
        case eventVolunteerBannerOpen = "event_volunteer_banner_open"

        //MARK: Donate/Give
        case donateOneTimeDonationStarted = "donation_one_time_started"
        case donateRecurringDonationStarted = "donation_recurring_started"

        //MARK: Forms
        case formViewDetails = "form_view_details" //segmentedFormInfoViewController
        case formStarted = "form_started"
        case formCompleted = "form_completed"

        ///MARK: SafetyNet
        case safetyNetChangeToLocationSearch = "safetynet_change_to_location_search"
        case safetyNetChangeToGlobalSearch = "safetynet_change_to_global_search"
        case safetyNetVisitWebsite = "safetynet_visit_website"
        case safetyNetVisitAddress = "safetynet_visit_address"
        case safetyNetCallPhone = "safetynet_call_phone"
        case safetyNetSearch = "safetynet_search"
        case safetyNetFacebookGroupVisit = "safetynet_visit_facebook_group"

        //MARK: About
        case newsletterSignupStarted = "newsletter_signup_started"
        case feedbackPositive = "feedback_positive"
        case feedbackComment = "feedback_comment"
        case feedbackReportProblem = "feedback_report_problem"

        //MARK: Learn More buttons
        case learnMorePressed = "learn_more_pressed"
     */