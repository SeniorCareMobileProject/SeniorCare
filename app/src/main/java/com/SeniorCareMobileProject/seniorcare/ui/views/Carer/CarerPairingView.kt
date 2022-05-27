package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.common.PermissionsPopup
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme

@Composable
fun FirstStartUpView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)

    ) {
        PermissionsPopup()
        Column(Modifier.weight(128f)) {
            NavButton(navController, "Login", "LoginScreen")
        }
        Column(Modifier.weight(128f)) {
            NavButton(navController, "Register", "RegisterScreen")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FirstStartUpViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        FirstStartUpView(navController, sharedViewModel)
    }
}

