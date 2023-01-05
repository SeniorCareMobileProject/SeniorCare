package com.wakeupgetapp.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.data.util.Resource
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SplashScreenWithLoading
import com.google.firebase.auth.AuthResult
import com.wakeupgetapp.seniorcare.R

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