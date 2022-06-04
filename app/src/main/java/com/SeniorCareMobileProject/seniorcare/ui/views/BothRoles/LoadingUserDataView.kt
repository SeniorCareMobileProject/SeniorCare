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
fun LoadingUserDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val loginState: State<Resource<AuthResult>?> = sharedViewModel.userSignUpStatus.observeAsState()
        val userDataState : State<Resource<User>?> = sharedViewModel.userDataStatus.observeAsState()

        when (loginState.value){
            is Resource.Loading<*> -> {
                CircularProgressIndicator()
            }
            is Resource.Success<*> -> {
                // read user function once from firebase
                LaunchedEffect(loginState){
                    sharedViewModel.getUserDataNew()
                }
                when (userDataState.value){
                    is Resource.Success<*> -> {
                        Log.d("Funkcja", sharedViewModel.userData.value!!.function.toString())
                        LaunchedEffect(userDataState){
                            navController.navigate("CarerMainScreen"){
                                popUpTo("LoadingUserDataView") {inclusive = true}
                            }
                        }
                    }
                    is Resource.Loading<*> -> {
                        CircularProgressIndicator()
                    }
                    else -> {Log.d("Funkcja", "null")}
                }

            }
            is Resource.Error<*> -> {
                LaunchedEffect(loginState){
                    Toast.makeText(context, "Login error - please enter a valid email and password", Toast.LENGTH_LONG).show()
                    navController.navigate("LoginScreen"){
                        popUpTo("LoadingUserDataView") {inclusive = true}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingLoginView() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}