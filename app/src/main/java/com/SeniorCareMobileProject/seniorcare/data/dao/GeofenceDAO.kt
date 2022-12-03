package com.SeniorCareMobileProject.seniorcare.data.dao

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GeofenceDAO(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val radius: Int? = null,
    val showAlarm: String? = null
)