package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*


@Composable
fun CarerSettingsListView(navController: NavController) {
    Scaffold(
        topBar = { TopBarSettings(navController, "Ustawienia - Piotr Kowalski") }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsItem(navController, "Przycisk SOS", "CarerSettingsSOSScreen")
            SettingsItem(navController, "Lokalizacja", "CarerSettingsSafeZoneScreen")
            SettingsItem(navController, "Rozłącz się z seniorem", "")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsListViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        CarerSettingsListView(navController)
    }
}