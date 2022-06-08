package com.SeniorCareMobileProject.seniorcare.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Screen
import com.SeniorCareMobileProject.seniorcare.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector
) {
    object Location : BottomNavItem(
        route = Screen.Location.route,
        titleResId = R.string.location,
        icon = Icons.Default.LocationOn
    )

    object Calendar : BottomNavItem(
        route = Screen.Calendar.route,
        titleResId = R.string.calendar,
        icon = Icons.Default.CalendarToday
    )

    object MedInfo : BottomNavItem(
        route = Screen.MedInfo.route,
        titleResId = R.string.medical_info,
        icon = Icons.Default.Info
    )

    object Notifications : BottomNavItem(
        route = Screen.Notifications.route,
        titleResId = R.string.notifications,
        icon = Icons.Default.Notifications
    )
}
