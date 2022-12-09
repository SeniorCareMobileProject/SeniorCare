package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.SeniorCareMobileProject.seniorcare.R


@Composable
fun CarerSettingsListView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBarSettings(navController, sharedViewModel) }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsItem(navController, sharedViewModel, context.getString(R.string.sos_btn_text), "CarerSettingsSOSScreen")
            SettingsItem(navController, sharedViewModel, context.getString(R.string.location_btn_text), "CarerSettingsSafeZoneScreen")
            SettingsItem(navController, sharedViewModel, context.getString(R.string.disconnect_btn_text), "")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsListViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        CarerSettingsListView(navController, sharedViewModel)
    }
}