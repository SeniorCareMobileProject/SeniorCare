package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.annotation.StringRes
import com.SeniorCareMobileProject.seniorcare.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Location : Screen("CarerMainScreen", R.string.location)
    object Calendar : Screen("CarerCalendarScreen", R.string.calendar)
    object MedInfo : Screen("CarerMedicalInfoScreen", R.string.medical_info)
    object Notifications : Screen("CarerNotificationsScreen", R.string.notifications)
}