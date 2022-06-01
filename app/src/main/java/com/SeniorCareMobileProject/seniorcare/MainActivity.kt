package com.SeniorCareMobileProject.seniorcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.*
import com.SeniorCareMobileProject.seniorcare.ui.views.TemplateView
import com.SeniorCareMobileProject.seniorcare.ui.views.TemplateView2

class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()

                NavHost(
                    navController,
                    startDestination = NavigationScreens.ChooseLoginMethodScreen.name,
                ) {
                    composable(NavigationScreens.ChooseLoginMethodScreen.name) {
                        ChooseLoginMethodView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.LoginScreen.name) {
                        LoginView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.RegisterScreen.name) {
                        RegisterView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.ChooseRoleScreen.name) {
                        ChooseRoleView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.ForgotPasswordScreen.name) {
                        ForgotPasswordView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.RegisterCodeScreen.name) {
                        RegisterView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerCreatingNotificationsScreen.name) {
                        CarerCreatingNotificationsView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerDayPlanningScreen.name) {
                        CarerDayPlanningView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerMainScreen.name) {
                        CarerMainView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerMedicalInfoScreen.name) {
                        CarerMedicalInfoView(navController, sharedViewModel)

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

