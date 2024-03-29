package com.SeniorCareMobileProject.seniorcare.data.dao

import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
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
    val sos: ArrayList<SosNumberDAO>? = null,
    val calendar: ArrayList<CalendarEventDAO>? = null,
    val notifications: ArrayList<NotificationItem>? = null,
    val trackingSettings: SeniorTrackingSettingsDao? = null,
    val battery: Float? = null,
    val latestNotification: NotificationItem? = null,
        )