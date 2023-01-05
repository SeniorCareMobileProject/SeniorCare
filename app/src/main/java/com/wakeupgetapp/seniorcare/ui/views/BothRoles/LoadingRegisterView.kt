package com.wakeupgetapp.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.wakeupgetapp.seniorcare.data.util.Resource
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.ui.views.Atoms.SplashScreenWithLoading
import com.google.firebase.auth.AuthResult
import com.wakeupgetapp.seniorcare.R

@Composable
fun LoadingRegisterView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val loginState: State<Resource<AuthResult>?> = sharedViewModel.userSignUpStatus.observeAsState()

        when (loginState.value){
            is Resource.Loading<*> -> {
                SplashScreenWithLoading(stringResource(id = R.string.loading_register))
            }
            is Resource.Success<*> -> {
                LaunchedEffect(loginState){
                    sharedViewModel.getUserData()
                    sharedViewModel.isAfterRegistration = true
                    navController.navigate("SignUpEmailVerificationScreen"){
                        popUpTo("LoadingRegisterView") {inclusive = true}
                    }
                }
            }
            is Resource.Error<*> -> {
                LaunchedEffect(loginState){
                    navController.navigate("SignUpEmailScreen"){
                        popUpTo("LoadingRegisterView") {inclusive = true}
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingRegisterView() {
    SeniorCareTheme() {
//        val navController = rememberNavController()
//        val sharedViewModel = SharedViewModel()
//        LoginView(navController, sharedViewModel)
        SplashScreenWithLoading(stringResource(id = R.string.loading_register))
    }
}