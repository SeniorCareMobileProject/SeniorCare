package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import android.app.Activity
import android.app.Service
import android.content.Intent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.fallDetector.FallDetectorService
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorFallDetectorSwitchButton


@Composable
fun SettingsFallDetectorView(navController: NavController, sharedViewModel: SharedViewModel) {
    Scaffold(topBar = {
        SeniorTopBar(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }) {
        val context = LocalContext.current
        val iconId = remember("elderly") {
            context.resources.getIdentifier(
                "elderly",
                "drawable",
                context.packageName
            )
        }
        val scrollState = remember { ScrollState(0) }
        val mCheckedState = remember { mutableStateOf(sharedViewModel.isFallDetectorTurnOn.value!!) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(42.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.fall_detector),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(
                        color = MaterialTheme.colors.primary
                    )
                )

                Spacer(modifier = Modifier.height(68.dp))

                Icon(
                    modifier = Modifier.size(270.dp),
                    painter = painterResource(id = iconId),
                    contentDescription = "elderly",
                    tint = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(80.dp))

                SeniorFallDetectorSwitchButton(text = stringResource(id = R.string.turn_on_off),
                    color = "main",
                    mCheckedState = mCheckedState,
                    sharedViewModel = sharedViewModel
                )

//            Column(
//                Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                // Creating a Button to display mCheckedState
//                // value in a Toast when clicked
//                Button(
//                    onClick = {
//                        Toast.makeText(context, mCheckedState.value.toString(), Toast.LENGTH_SHORT)
//                            .show()
//                    },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58)),
//                ) {
//                    Text("Show Checked State", color = Color.White)
//                }
//            }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsFallDetectorViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SettingsFallDetectorView(navController, sharedViewModel)
    }
}