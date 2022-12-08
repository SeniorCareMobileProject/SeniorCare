package com.SeniorCareMobileProject.seniorcare.data.dao

data class SeniorTrackingSettingsDao(
    var carerOpenApp: Boolean = false,
    var seniorInSafeZone: Boolean = true,
    var seniorIsAware: Boolean = false
)