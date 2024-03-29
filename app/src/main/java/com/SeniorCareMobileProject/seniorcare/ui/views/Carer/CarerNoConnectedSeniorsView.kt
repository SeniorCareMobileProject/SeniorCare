package com.SeniorCareMobileProject.seniorcare.ui.views.Carer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@Composable
fun CarerNoConnectedSeniorsView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = context.getString(R.string.caution),
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
        )
        Text(
            text = context.getString(R.string.not_connected_to_any_user_text),
            color = Color.Black,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(20.dp),
        )
        Button(
            modifier = Modifier.padding(20.dp),
            onClick = {
                sharedViewModel.createPairingCode()
                navController.navigate("PairingScreenCodeScreen"){
                    popUpTo("CarerNoConnectedSeniorsView") {inclusive = true}
                }
            })
        {
            Text(context.getString(R.string.go_to_the_pairing_screen))
        }
    }
}

@Preview
@Composable
fun CarerNoConnectedSeniorsViewPreview() {
    CarerNoConnectedSeniorsView(navController = NavController(LocalContext.current), sharedViewModel = SharedViewModel())
}