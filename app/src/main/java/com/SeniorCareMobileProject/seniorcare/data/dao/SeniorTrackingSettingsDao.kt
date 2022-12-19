package com.SeniorCareMobileProject.seniorcare.data.dao

import com.google.firebase.database.PropertyName

data class SeniorTrackingSettingsDao(
    var carerOpenApp: Boolean = false,
    var seniorInSafeZone: Boolean = true,
    @get:PropertyName("isSeniorAware")
    @set:PropertyName("isSeniorAware")
    var isSeniorAware: Boolean = false,
)