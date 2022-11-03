package com.SeniorCareMobileProject.seniorcare.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class CalendarEvent(
    var date: LocalDate,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var eventName: String,
    var eventDescription: String? = null,
)

val emptyEvent = CalendarEvent(
    LocalDate(0, 1, 1),
    LocalTime(0, 0),
    LocalTime(0, 0),
    "",
    ""
)