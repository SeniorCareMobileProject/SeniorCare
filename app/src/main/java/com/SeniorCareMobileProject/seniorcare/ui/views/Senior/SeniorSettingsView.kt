package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButton

@Composable
fun SeniorSettingsView(navController: NavController, sharedViewModel: SharedViewModel) {
    Scaffold(topBar = {
        SeniorTopBar(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 54.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Text(
                text = stringResource(id = R.string.settings),
                fontWeight = FontWeight.Medium,
                fontSize = 36.sp,
            )

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                SeniorButton(
                    navController = navController,
                    text = stringResource(id = R.string.add_carer),
                    iconName = "add_circle",
                    rout = "PairingScreenCodeInputScreen",
                    sharedViewModel = sharedViewModel
                )
                SeniorButton(
                    navController = navController,
                    text = stringResource(id = R.string.fall_detector),
                    iconName = "elderly",
                    rout = "SettingsFallDetectorScreen",
                    sharedViewModel = sharedViewModel
                )
                SeniorButton(
                    navController = navController,
                    text = stringResource(id = R.string.carers_list),
                    iconName = "format_list_bulleted",
                    rout = "SeniorCarersListScreen",
                    sharedViewModel = sharedViewModel
                )
                SeniorButton(
                    navController = navController,
                    text = stringResource(id = R.string.log_out),
                    iconName = "logout",
                    rout = "sign out",
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorSettingsViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SeniorSettingsView(navController, sharedViewModel = SharedViewModel())
    }
}
