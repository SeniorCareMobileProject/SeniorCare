package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.BottomNavItem
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.CarerCalendarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.CarerMainView
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.CarerMedicalInfoView
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.CarerNotificationsView
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationSetup(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    NavHost(navController, startDestination = BottomNavItem.Location.route) {
        composable(BottomNavItem.Location.route) {
            CarerMainView(navController, sharedViewModel, scope, scaffoldState)
        }
        composable(BottomNavItem.Calendar.route) {
            CarerCalendarView(navController, sharedViewModel, scope, scaffoldState)
        }
        composable(BottomNavItem.MedInfo.route) {
            CarerMedicalInfoView(navController, sharedViewModel, scope, scaffoldState)
        }
        composable(BottomNavItem.Notifications.route) {
            CarerNotificationsView(navController, sharedViewModel, scope, scaffoldState)
        }
    }
}
