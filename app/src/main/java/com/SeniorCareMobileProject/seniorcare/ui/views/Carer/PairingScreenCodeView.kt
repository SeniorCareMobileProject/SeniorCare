package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.android.material.progressindicator.CircularProgressIndicator


@Composable
fun PairingScreenCodeView(navController: NavController, sharedViewModel: SharedViewModel) {
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
                        .clickable { navController.navigate(NavigationScreens.LoginScreen.name) }
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 181.dp)
                .padding(horizontal = 43.dp)
        ) {
            Text(
                text = "Wprowadź poniższą liczbę na ekranie parowania podopiecznego.",
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 57.dp)
                .padding(horizontal = 117.dp)
        ) {
            val pairingCode by sharedViewModel.pairingCode.observeAsState()
            when (pairingCode){
                "" -> {
                    CircularProgressIndicator()
                    sharedViewModel.createPairingCode()
                }
                else -> PairingCodeText(sharedViewModel.pairingCode)
            }
        }
    }

    // CHECK IF PAIRING IS DONE
    val pairingState : State<Boolean?> = sharedViewModel.pairingStatus.observeAsState()
    val writeNewConnectionStatus: State<Resource<String>?> =  sharedViewModel.writeNewConnectionStatus.observeAsState()
    val currentSeniorDataStatus : State<Resource<User>?> = sharedViewModel.currentSeniorDataStatus.observeAsState()
    when (pairingState.value){
        true -> {
            LaunchedEffect(pairingState){
                sharedViewModel.getSeniorIDForPairing()
            }
            when (writeNewConnectionStatus.value){
                is Resource.Success<*> -> {
                    LaunchedEffect(writeNewConnectionStatus){
                        sharedViewModel.writeNewConnectionWith(sharedViewModel.pairingSeniorID.value!!)
                        sharedViewModel.deletePairingCode()
                        sharedViewModel.getCurrentSeniorData()
                    }
                    when (currentSeniorDataStatus.value) {
                        is Resource.Success<*> -> {
                            LaunchedEffect(currentSeniorDataStatus) {
                                navController.navigate("PairingScreenSuccessScreen") {
                                    popUpTo("PairingScreenCodeScreen") { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PairingCodeText(text: MutableLiveData<String?>) {
    val pairingCode by text.observeAsState()

    pairingCode?.let {
        Text(
        text = it,
        color = MaterialTheme.colors.primary,
        fontSize = 44.sp,
        fontWeight = FontWeight.Medium
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PairingScreenCodeViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PairingScreenCodeView(navController, sharedViewModel)
    }
}
