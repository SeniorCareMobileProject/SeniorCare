package com.wakeupgetapp.seniorcare.ui.views.Senior

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.IconTextButton
import com.wakeupgetapp.seniorcare.ui.views.Atoms.UserCard

@Composable
fun PairingScreenConfirmationView(navController: NavController, sharedViewModel: SharedViewModel) {
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
                        .clickable { navController.navigateUp() }
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 179.dp)
                .padding(horizontal = 43.dp)
        ) {
            Text(
                text = stringResource(id = R.string.confirm_pairing),
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 64.dp)
        ) {
            UserCard(
                navController = navController,
                name = "${sharedViewModel.pairingData.value?.firstName} ${sharedViewModel.pairingData.value?.lastName}",
                emial = "${sharedViewModel.pairingData.value?.email}"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 95.dp)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PairingConfirmButton(navController = navController, text = stringResource(id = R.string.yes), rout = "PairingScreenSeniorSuccessScreen", sharedViewModel)
            IconTextButton(navController = navController, text = stringResource(id = R.string.no), iconName = "", rout = "PairingScreenCodeInputScreen")
        }
    }
}

@Composable
fun PairingConfirmButton(navController: NavController, text: String, rout: String, sharedViewModel: SharedViewModel) {
    Button(
        onClick = {
            sharedViewModel.writeSeniorIDForPairing()
            sharedViewModel.writeNewConnectionWith(sharedViewModel.pairingData.value!!.carerID!!)
            navController.navigate(rout)
            sharedViewModel.updatePairingStatus()
                  },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PairingScreenConfirmationViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PairingScreenConfirmationView(navController, sharedViewModel)
    }
}
