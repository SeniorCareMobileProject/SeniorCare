package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun DaySeparator(navController: NavController, text: String) {
    Column(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(Color(0xFFCAAAF9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 28.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SeniorCalendarViewAtomsPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        DaySeparator(navController, "Dzisiaj")
    }
}