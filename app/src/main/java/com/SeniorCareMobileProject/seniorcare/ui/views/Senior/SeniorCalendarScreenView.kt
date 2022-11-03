package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

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
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun SeniorCalendarScreenView(navController: NavController) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorCalendarScreenPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SeniorCalendarScreenView(navController)
    }
}