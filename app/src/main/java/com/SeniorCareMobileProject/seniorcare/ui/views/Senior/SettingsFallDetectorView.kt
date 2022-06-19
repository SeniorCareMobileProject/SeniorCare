package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import android.widget.Space
import android.widget.Switch
import android.widget.Toast
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
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorSwitchButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun SettingsFallDetectorView(navController: NavController) {
    val context = LocalContext.current
    val iconId = remember("elderly") {
        context.resources.getIdentifier(
            "elderly",
            "drawable",
            context.packageName
        )
    }
    val scrollState = remember { ScrollState(0) }
    val mCheckedState = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigate(NavigationScreens.SeniorSettingsScreen.name) }
        )

        Spacer(modifier = Modifier.height(42.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detektor upadku",
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

            SeniorSwitchButton(text = "Włącz/wyłącz",
                color = "main",
                mCheckedState = mCheckedState
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsFallDetectorViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SettingsFallDetectorView(navController)
    }
}