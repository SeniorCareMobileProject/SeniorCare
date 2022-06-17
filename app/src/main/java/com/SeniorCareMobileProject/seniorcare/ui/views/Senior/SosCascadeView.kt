package com.SeniorCareMobileProject.seniorcare.ui.views.Senior


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*

@Composable
fun SosCascadeView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .onPlaced { sharedViewModel.timer.start() }
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
                    text = "Masz 10 sekund",
                    iconName = "clear",
                    sharedViewModel = sharedViewModel,
                    color = "red"
                )
                SosCascadeStop(
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



