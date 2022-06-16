package com.SeniorCareMobileProject.seniorcare.ui.views.Senior


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SosButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SosButton2

@Composable
fun SosCascadeView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                SosButton(
                    navController = navController,
                    text = "Dzwoń dalej",
                    iconName = "clear",
                    sharedViewModel = sharedViewModel,
                    color = "red"
                )
                SosButton2(
                    navController = navController,
                    text = "Nie dzwoń",
                    iconName = "add_circle",
                    sharedViewModel = sharedViewModel,
                    color = ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SosCascadeViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SeniorNotificationView(navController, sharedViewModel)
    }
}
