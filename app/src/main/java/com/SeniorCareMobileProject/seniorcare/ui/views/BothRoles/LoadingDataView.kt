package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.auth.AuthResult

@Composable
fun LoadingDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val userDataState : State<Resource<User>?> = sharedViewModel.userDataStatus.observeAsState()

        when (userDataState.value){
            is Resource.Success<*> -> {
                Log.d("Funkcja", sharedViewModel.userData.value!!.function.toString())
                LaunchedEffect(userDataState){
                    if (sharedViewModel.userData.value!!.function == "Carer"){
                        navController.navigate("CarerMainScreen"){
                            popUpTo("LoadingDataView") {inclusive = true}
                        }
                    }
                    else if (sharedViewModel.userData.value!!.function == "Senior"){
                        navController.navigate("SeniorMainScreen"){
                            popUpTo("LoadingDataView") {inclusive = true}
                        }
                    }
                }
            }
            is Resource.Loading<*> -> {
                CircularProgressIndicator()
            }
            else -> {Log.d("Funkcja", "null")}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingDataView() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}