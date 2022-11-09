package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SosCascadeStartButton

@Composable
fun SeniorMainView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        Text(
            text = "Witaj ${sharedViewModel.userData.value?.firstName}",
            fontWeight = FontWeight.Medium,
            fontSize = 36.sp,
            color = MaterialTheme.colors.primary
        )

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            val comeHomeColor: String
            val comeHomeText: String
            if (/* wyszedł z domu */ false) {
                comeHomeColor = "came_home"
                comeHomeText = "Wróciłem do domu"
            } else {
                comeHomeColor = "main"
                comeHomeText = "Wychodzę z domu"
            }

            SeniorButton(
                navController = navController,
                text = comeHomeText,
                iconName = "my_location",
                rout = "SeniorGoingOutInfoScreen",
                color = comeHomeColor
            )
            SeniorButton(
                navController = navController,
                text = "Informacje medyczne",
                iconName = "info",
                rout = "SeniorMedicalInfoScreen",
                color = "main"
            )
            SeniorButton(
                navController = navController,
                text = "Kalendarz",
                iconName = "calendar_month",
                rout = "SeniorCalendarScreen",
                color = "main"
            )
//            SeniorButton(
//                navController = navController,
//                text = "Detektor upadku",
//                iconName = "clear",
//                rout = "",
//                color = "main"
//            )
            /*SeniorButton(
            SeniorButton(
                navController = navController,
                text = "Detektor upadku",
                iconName = "clear",
                rout = "",
                color = "main"
            )*/
            /**SeniorButton(
                navController = navController,
                text = "SOS",
                iconName = "clear",
                rout = "SosCascadeView",
                color = "red"
            )**/
            SosCascadeStartButton(
                navController = navController,
                text = "SOS",
                iconName = "clear",
                sharedViewModel = sharedViewModel,
                color = "red",
                rout = "SosCascadeView"
            )
            SeniorButton(
                navController = navController,
                text = "Ustawienia",
                iconName = "settings",
                rout = "SeniorSettingsScreen",
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorMainViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SeniorMainView(navController,sharedViewModel)
    }
}




