package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.RoundSmallButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.RoundSmallButtonBack
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.RoundSmallButtonDrawer
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CarerAboutAppView(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBarAboutApp(navController) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
        ) {
            Text(
                context.getString(R.string.about_app),
                textAlign = TextAlign.Justify
            )
            Column(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(20.dp)
            )
            {
                Text(context.getString(R.string.hyphen, R.string.space, R.string.Lukasz_Koziej))
                Text(context.getString(R.string.hyphen, R.string.space, R.string.Adrian_Olkowicz))
                Text(context.getString(R.string.hyphen, R.string.space, R.string.Michal_Dudziak))
                Text(context.getString(R.string.hyphen, R.string.space, R.string.Andrei_Strachynski))
            }
            Text(
                context.getString(R.string.about_app_2),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun TopBarAboutApp(
    navController: NavController
) {
    val context = LocalContext.current

    TopAppBar(backgroundColor = Color(0xFFCAAAF9)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
//                .padding(top = 11.dp, bottom = 11.dp)
//                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundSmallButtonBack(
                    navController = navController,
                    iconName = "keyboard_backspace",
                )
            }
            Text(
                text = context.getString(R.string.application_information),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                RoundSmallButton(
                    navController = navController,
                    iconName = "settings",
                    rout = "CarerSettingsListScreen"
                )
            }
        }
    }
}

@Preview
@Composable
fun CarerAboutAppViewPreview() {
//    CarerAboutAppView(
//        navController = NavController(LocalContext.current),
//        sharedViewModel = SharedViewModel(),
//        scope = CoroutineScope(),
//        scaffoldState = ScaffoldState()
//    )
}