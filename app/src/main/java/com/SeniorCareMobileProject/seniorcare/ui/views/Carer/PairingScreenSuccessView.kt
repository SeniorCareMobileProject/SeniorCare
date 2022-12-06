package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SmallButtonWithRout


@Composable
fun PairingScreenSuccessView(navController: NavController, sharedViewModel: SharedViewModel) {
    // Background
    val context = LocalContext.current
    val ellipse1 = remember("ellipse_12") {
        context.resources.getIdentifier(
            "ellipse_12",
            "drawable",
            context.packageName
        )
    }
    val ellipse2 = remember("ellipse_14") {
        context.resources.getIdentifier(
            "ellipse_14",
            "drawable",
            context.packageName
        )
    }
    val ellipse3 = remember("subtract") {
        context.resources.getIdentifier(
            "subtract",
            "drawable",
            context.packageName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Column() {
            Icon(
                painter = painterResource(id = ellipse1),
                contentDescription = null,
                modifier = Modifier
                    .width(133.dp)
                    .height(133.dp)
                    .offset(x = -2.dp),
                tint = MaterialTheme.colors.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    painter = painterResource(id = ellipse2),
                    contentDescription = null,
                    modifier = Modifier
                        .width(226.dp)
                        .height(121.dp)
                        .offset(x = 130.dp),
                    tint = Color(0xFFF1ECF8)
                )
                Icon(
                    painter = painterResource(id = ellipse3),
                    contentDescription = null,
                    modifier = Modifier
                        .width(151.dp)
                        .height(174.dp)
                        .offset(x = 17.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {

        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .padding(horizontal = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clickable { navController.navigate(NavigationScreens.PairingScreenCodeScreen.name) }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 242.dp)
                .padding(horizontal = 82.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Udało się!",
                color = MaterialTheme.colors.primary,
                fontSize = 44.sp,
                fontWeight = FontWeight.Medium
            )
        }

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 38.dp)
//                .padding(horizontal = 43.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Sparowano z użytkownikiem:",
//                fontWeight = FontWeight.Normal
//            )
//            Text(
//                text = "${sharedViewModel.currentSeniorData.value?.firstName} ${sharedViewModel.currentSeniorData.value?.lastName}",
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = "${sharedViewModel.currentSeniorData.value?.email}",
//                fontWeight = FontWeight.Normal
//            )
//        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            SmallButtonWithRout(navController, "Ok", "CarerMainScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PairingScreenSuccessViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PairingScreenSuccessView(navController, sharedViewModel)
    }
}
