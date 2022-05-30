package com.SeniorCareMobileProject.seniorcare.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.SignUpVerificationCodeView


@Composable
fun NameView(navController: NavController, sharedViewModel: SharedViewModel) {

}

@Preview(showBackground = true)
@Composable
fun NameViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        NameView(navController, sharedViewModel)
    }
}