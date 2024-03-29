package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.PairingData
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.LoginView
import com.google.firebase.auth.AuthResult

@Composable
fun LoadingPairingDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pairingDataState : State<Resource<PairingData>?> = sharedViewModel.pairingDataStatus.observeAsState()

        when (pairingDataState.value){
            is Resource.Loading<*> -> {
                CircularProgressIndicator()
            }
            is Resource.Success<*> -> {
                LaunchedEffect(pairingDataState){
                    navController.navigate("PairingScreenConfirmationScreen"){
                        popUpTo("LoadingPairingDataView") {inclusive = true}
                    }
                    Log.d("Pairing Data", sharedViewModel.pairingData.value!!.email!!)
                }
            }
            is Resource.Error<*> -> {
                LaunchedEffect(pairingDataState){
                    navController.navigate("PairingScreenCodeInputScreen"){
                        popUpTo("LoadingPairingDataView") {inclusive = true}
                    }
                    Toast.makeText(context, context.getString(R.string.incorrect_pairing_code), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPairingDataView() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}