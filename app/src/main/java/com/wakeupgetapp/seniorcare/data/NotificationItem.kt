package com.wakeupgetapp.seniorcare.data



data class NotificationItem(
    var name: String = "",
    var timeList: MutableList<String> = mutableListOf(),
    var interval: String = ""
)