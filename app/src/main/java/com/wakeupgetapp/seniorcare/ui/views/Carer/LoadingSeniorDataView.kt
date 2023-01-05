package com.wakeupgetapp.seniorcare.ui.views.Carer

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
import com.wakeupgetapp.seniorcare.data.dao.User
import com.wakeupgetapp.seniorcare.data.util.Resource
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.BothRoles.LoginView

@Composable
fun LoadingSeniorDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val currentSeniorDataStatus : State<Resource<User>?> = sharedViewModel.currentSeniorDataStatus.observeAsState()

        when (currentSeniorDataStatus.value){
            is Resource.Success<*> -> {
                LaunchedEffect(currentSeniorDataStatus){
                            navController.navigate("CarerMainScreen"){
                                popUpTo("LoadingSeniorDataView") {inclusive = true}
                            }
                        }
                }
            is Resource.Loading<*> -> {
                CircularProgressIndicator()
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingSeniorDataView() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}