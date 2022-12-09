package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapsAddGeofenceComponent
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.RadiusChanger
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBarSettingsSafeZone


@Composable
fun CarerSettingsSafeZoneView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopBarSettingsSafeZone(
                navController,
                sharedViewModel,
                context.getString(R.string.localization_safe_zone)
            )
        }) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(494f)) {
                MapsAddGeofenceComponent(sharedViewModel = sharedViewModel)
            }

            Column(
                Modifier
                    .weight(161f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                RadiusChanger(navController = navController, sharedViewModel = sharedViewModel)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsSafeZoneViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
//        CarerSettingsSafeZoneView(navController)
    }
}