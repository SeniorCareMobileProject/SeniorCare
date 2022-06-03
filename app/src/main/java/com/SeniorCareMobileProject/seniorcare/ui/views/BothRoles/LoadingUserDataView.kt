package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme

@Composable
fun LoadingUserDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    //sharedViewModel.getUserData()
    sharedViewModel.readUserFunction()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        CheckUserFunctionAndNavigate(navController, sharedViewModel)
    }
}

@Composable
fun CheckUserFunctionAndNavigate(navController: NavController, sharedViewModel: SharedViewModel) {
    val userFunction by sharedViewModel.userFunction.observeAsState()
    if (userFunction != null){
        Log.d("Funkcja", sharedViewModel.userFunction.value.toString())
        if (sharedViewModel.userFunction.value == "Carer"){
            LaunchedEffect(userFunction){
                //sharedViewModel.setUserFunction(null)
                navController.navigate("CarerMainScreen"){
                    popUpTo("LoadingUserDataView") {inclusive = true}
                }
            }
        }
        else if (sharedViewModel.userFunction.value == "Senior"){
            LaunchedEffect(userFunction){
                //sharedViewModel.setUserFunction(null)
                navController.navigate("SeniorMainScreen"){
                    popUpTo("LoadingUserDataView") {inclusive = true}
                }
            }
        }
        else {
            LaunchedEffect(userFunction){
                //sharedViewModel.setUserFunction(null)
                navController.navigate("LoginScreen"){
                    popUpTo("LoadingUserDataView") {inclusive = true}
                }
            }
        }
    }
    else {
        Log.d("Funkcja", "brak danych")
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingUserDataView() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}