package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButtonNoIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun SeniorGoingOutInfoView(navController: NavController, sharedViewModel: SharedViewModel) {
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
                .background(Color(0xFFF1ECF8))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(vertical = 44.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.going_out_text),
                            fontSize = 28.sp, fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                SeniorButtonNoIcon(
                    navController = navController,
                    text = stringResource(id = R.string.i_came_home),
                    rout = "SeniorMainScreen",
                    color = "came_home"
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorGoingOutInfoViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
//        SeniorGoingOutInfoView(navController)
    }
}