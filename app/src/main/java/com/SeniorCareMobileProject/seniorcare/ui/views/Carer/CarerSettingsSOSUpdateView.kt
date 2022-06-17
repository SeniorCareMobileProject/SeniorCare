package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SettingsItem
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SettingsItemWithIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SettingsNumberElement
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBarSettings


@Composable
fun CarerSettingsSOSUpdateView(navController: NavController) {
    Scaffold(
        topBar = { TopBarSettings(navController, "Przycisk Sos - Piotr Kowalski") }) {
        Column(modifier = Modifier.fillMaxSize()) {
            SettingsItem(navController, "Dodaj numer", "")
            SettingsItemWithIcon(
                navController,
                "Lista opiekunów",
                "CarerSettingsSOSUpdateScreen",
                "edit"
            )
            SettingsNumberElement("Paweł", 123456789, true)
            SettingsNumberElement("Agnieszka", 123456789, true)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarerSettingsSOSUpdateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        CarerSettingsSOSUpdateView(navController)
    }
}