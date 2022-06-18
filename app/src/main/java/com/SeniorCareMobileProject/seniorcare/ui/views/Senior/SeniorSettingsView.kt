package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButton

@Composable
fun SeniorSettingsView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Text(
            text = "Ustawienia",
            fontWeight = FontWeight.Medium,
            fontSize = 36.sp,
        )

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            SeniorButton(
                navController = navController,
                text = "Dodaj opiekuna",
                iconName = "add_circle",
                rout = "",
            )
            SeniorButton(
                navController = navController,
                text = "Detektor upadku",
                iconName = "elderly",
                rout = "",
            )
            SeniorButton(
                navController = navController,
                text = "Lista opiekónów",
                iconName = "format_list_bulleted",
                rout = "",
            )
            SeniorButton(
                navController = navController,
                text = "Wyloguj się",
                iconName = "logout",
                rout = "",
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorSettingsViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SeniorSettingsView(navController)
    }
}
