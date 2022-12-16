package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SOSSettingsItemWithIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SettingsEditNumberElement
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SettingsItemWithIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBarSettings


@Composable
fun CarerSettingsSOSUpdateView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBarSettings(navController, sharedViewModel) }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SOSSettingsItemWithIcon(
                navController,
                sharedViewModel,
                context.getString(R.string.save_changes),
                "CarerSettingsSOSScreen",
                "done"
            )
            for(i in 0 until sharedViewModel.sosCascadePhoneNumbers.size){
                SettingsEditNumberElement(index = i, sharedViewModel = sharedViewModel)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsSOSUpdateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        CarerSettingsSOSUpdateView(navController, sharedViewModel)
    }
}