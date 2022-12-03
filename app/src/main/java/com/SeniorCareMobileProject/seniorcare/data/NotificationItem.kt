package com.SeniorCareMobileProject.seniorcare.data



data class NotificationItem(
    var name: String,
    var timeList: MutableList<String>,
    var interval: String
)