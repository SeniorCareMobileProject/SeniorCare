package com.wakeupgetapp.seniorcare.ui.views.Senior

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.data.LocalSettingsRepository
import com.wakeupgetapp.seniorcare.data.Repository
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SeniorButton
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SeniorButtonWithAction
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SosCascadeStartButton
import com.wakeupgetapp.seniorcare.R

@Composable
fun SeniorMainView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.welcome) + " ${sharedViewModel.userData.value?.firstName}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 36.sp,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )

                val comeHomeColor: String
                val comeHomeText: String
                if (/* wyszedł z domu */ false) {
                    comeHomeColor = "came_home"
                    comeHomeText = stringResource(id = R.string.i_came_home)
                } else {
                    comeHomeColor = "main"
                    comeHomeText = stringResource(id = R.string.i_am_going_out)
                }
                var context = LocalContext.current
                Column(modifier = Modifier.weight(1f)) {
                    SeniorButtonWithAction(
                        navController = navController,
                        text = comeHomeText,
                        iconName = "my_location",
                        rout = "SeniorGoingOutInfoScreen",
                        color = comeHomeColor,
                        sharedViewModel = sharedViewModel
                    ) {
                        Repository().saveTrackingSettingsSenior(null, true)
                        val localSettingsRepository = LocalSettingsRepository.getInstance(context)
                        localSettingsRepository.saveSeniorIsAware(true)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    SeniorButton(
                        navController = navController,
                        text = stringResource(id = R.string.medical_info),
                        iconName = "info",
                        rout = "SeniorMedicalInfoScreen",
                        color = "main",
                        sharedViewModel = sharedViewModel
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SeniorButton(
                        navController = navController,
                        text = stringResource(id = R.string.calendar),
                        iconName = "calendar_month",
                        rout = "SeniorCalendarScreen",
                        color = "main",
                        sharedViewModel = sharedViewModel
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SeniorButton(
                        navController = navController,
                        text = stringResource(id = R.string.fall_detector),
                        iconName = "elderly",
                        rout = "SettingsFallDetectorScreen",
                        color = "main",
                        sharedViewModel = sharedViewModel
                    )
                }
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
                Column(modifier = Modifier.weight(1f)) {
                    SosCascadeStartButton(
                        navController = navController,
                        text = stringResource(id = R.string.sos),
                        iconName = "clear",
                        sharedViewModel = sharedViewModel,
                        color = "red",
                        rout = "SosCascadeView"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SeniorButton(
                        navController = navController,
                        text = stringResource(id = R.string.settings),
                        iconName = "settings",
                        rout = "SeniorSettingsScreen",
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorMainViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SeniorMainView(navController, sharedViewModel)
    }
}




