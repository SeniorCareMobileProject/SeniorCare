package com.SeniorCareMobileProject.seniorcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.BottomNavItem
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.BottomNavigationBarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.Drawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.NavigationSetup
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.*

class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

                NavHost(
                    navController,
                    startDestination = NavigationScreens.ChooseLoginMethodScreen.name,
                ) {
                    composable(BottomNavItem.Location.route) {
                        CarerMainView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.Calendar.route) {
                        CarerDayPlanningView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.MedInfo.route) {
                        CarerMedicalInfoView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.Notifications.route) {
                        CarerNotificationsView(navController, sharedViewModel, scope, scaffoldState)
                    }


                    composable(NavigationScreens.ChooseLoginMethodScreen.name) {
                        ChooseLoginMethodView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.LoginScreen.name) {
                        LoginView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.CarerPairingScreen.name) {
                        CarerPairingView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorCalendarScreen.name) {
                        SeniorCalendarView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMainScreen.name) {
                        SeniorMainView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMedicalInfoScreen.name) {
                        SeniorMedicalInfoView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorNotificationScreen.name) {
                        SeniorNotificationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpGoogleScreen.name) {
                        SignUpGoogleView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpEmailScreen.name) {
                        SignUpEmailView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpEmailVerificationScreen.name) {
                        SignUpEmailVerificationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PasswordRecoveryScreen.name) {
                        PasswordRecoveryView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PasswordRecoveryEmailScreen.name) {
                        PasswordRecoveryEmailView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenCodeScreen.name) {
                        PairingScreenCodeView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenSuccessScreen.name) {
                        PairingScreenSuccessView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenCodeInputScreen.name) {
                        PairingScreenCodeInputView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenConfirmationScreen.name) {
                        PairingScreenConfirmationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenSeniorSuccessScreen.name) {
                        PairingScreenSeniorSuccessView(navController, sharedViewModel)

                    }


                }

            }
        }
    }
}

