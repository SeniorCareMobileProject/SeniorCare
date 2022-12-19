package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SplashScreenWithLoading
import com.google.firebase.auth.AuthResult

@Composable
fun LoadingLoginView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val loginState: State<Resource<AuthResult>?> = sharedViewModel.userSignUpStatus.observeAsState()

        when (loginState.value){
            is Resource.Loading<*> -> {
                SplashScreenWithLoading(stringResource(id = R.string.loading_login))
            }
            is Resource.Success<*> -> {
                LaunchedEffect(loginState){
                    sharedViewModel.getUserData()
                    navController.navigate("LoadingDataView"){
                        popUpTo("LoadingLoginView") {inclusive = true}
                    }
                }
            }
            is Resource.Error<*> -> {
                LaunchedEffect(loginState){
                    navController.navigate("LoginScreen"){
                        popUpTo("LoadingLoginView") {inclusive = true}
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