package com.SeniorCareMobileProject.seniorcare.data.dao

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class SeniorAllDAO (
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val function: String? = null,
    val location: LocationDAO? = null,
    val geofence: GeofenceDAO? = null,
    val medicalInformation: MedInfoDAO? = null,
        )