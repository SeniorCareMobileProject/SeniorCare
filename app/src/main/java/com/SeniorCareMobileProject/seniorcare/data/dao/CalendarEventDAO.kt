package com.SeniorCareMobileProject.seniorcare.data.dao


data class CalendarEventDAO(
    var date: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var eventName: String = "",
    var eventDescription: String? = null,
)
