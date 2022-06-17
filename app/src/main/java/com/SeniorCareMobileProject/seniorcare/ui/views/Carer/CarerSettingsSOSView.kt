package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun CarerSettingsSOSView(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBarSettings(navController, "Przycisk Sos - Piotr Kowalski") }) {

        if (showDialog.value) {
            NewContactPopupView(setShowDialog = { showDialog.value = it })
        }

        Column(modifier = Modifier.fillMaxSize()) {
            AddNumber(
                navController,
                "Dodaj numer",
                setShowDialog = { showDialog.value = it }
            )
            SettingsItemWithIcon(
                navController,
                "Lista opiekunów",
                "CarerSettingsSOSUpdateScreen",
                "edit"
            )
            SettingsNumberElement("Paweł", 123456789, false)
            SettingsNumberElement("Agnieszka", 123456789, false)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsSOSViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        CarerSettingsSOSView(navController)
    }
}