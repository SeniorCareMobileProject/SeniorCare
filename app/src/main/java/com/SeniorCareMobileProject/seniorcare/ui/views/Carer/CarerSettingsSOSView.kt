package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*


@Composable
fun CarerSettingsSOSView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBarSettings(navController, sharedViewModel) }) {

        if (showDialog.value) {
            NewContactPopupView(setShowDialog = { showDialog.value = it },sharedViewModel)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            AddNumber(
                navController,
                context.getString(R.string.add_number_btn_text),
                setShowDialog = { showDialog.value = it }
            )
            SOSSettingsItemWithIcon(
                navController,
                sharedViewModel,
                context.getString(R.string.carers_list),
                "CarerSettingsSOSUpdateScreen",
                "edit"
            )
            for(i in 0 until sharedViewModel.sosCascadePhoneNumbers.size){
                SettingsNumberElement(index = i, sharedViewModel = sharedViewModel, navController, "CarerSettingsSOSScreen")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsSOSViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        CarerSettingsSOSView(navController, sharedViewModel)
    }
}