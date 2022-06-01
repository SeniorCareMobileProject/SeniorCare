package com.SeniorCareMobileProject.seniorcare.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.SignUpVerificationCodeView


@Composable
fun PairingScreenCodeView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigate(NavigationScreens.ChooseLoginMethodScreen.name) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PairingScreenCodeViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PairingScreenCodeView(navController, sharedViewModel)
    }
}