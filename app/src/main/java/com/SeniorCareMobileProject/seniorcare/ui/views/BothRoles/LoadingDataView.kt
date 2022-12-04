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
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoadingDataView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val userDataState : State<Resource<User>?> = sharedViewModel.userDataStatus.observeAsState()
        val context = LocalContext.current

        when (userDataState.value){
            is Resource.Success<*> -> {
                Log.d("Funkcja", sharedViewModel.userData.value!!.function.toString())
                LaunchedEffect(userDataState){
                    if (sharedViewModel.userData.value?.connectedWith == null){
                        sharedViewModel.isAfterRegistration = true
                    }
                    if (sharedViewModel.userData.value!!.function == "Carer"){
                        if (sharedViewModel.isAfterRegistration){
                            // CREATE PAIRING
                            sharedViewModel.createPairingCode()
                            navController.navigate("PairingScreenCodeScreen"){
                                popUpTo("LoadingDataView") {inclusive = true}
                            }
                        }
                        else {
                            sharedViewModel.getCurrentSeniorData()
                            navController.navigate("LoadingSeniorDataView"){
                                popUpTo("LoadingDataView") {inclusive = true}
                            }
                        }
                    }
                    else if (sharedViewModel.userData.value!!.function == "Senior"){
                        if (sharedViewModel.isAfterRegistration){
                            navController.navigate("PairingScreenCodeInputScreen"){
                                popUpTo("LoadingDataView") {inclusive = true}
                            }
                        }
                        else {
                            // get geofence information
                            sharedViewModel.getGeofenceForSenior()
                            sharedViewModel.getMedicalInformationForSenior()
                            sharedViewModel.getSosNumbersForSenior()
                            sharedViewModel.loadCalendarEventsFromFirebase()
                            sharedViewModel.getNotificationsForSenior()
                            navController.navigate("SeniorMainScreen"){
                                popUpTo("LoadingDataView") {inclusive = true}
                            }
                        }
                    }
                }
            }
            is Resource.Loading<*> -> {
                CircularProgressIndicator()
            }
            else -> {
                Log.d("Error getting data", "brak danych")
                if (sharedViewModel.isDataEmpty.value == true){
                    sharedViewModel.isDataEmpty.value = false
                    //FirebaseAuth.getInstance().signOut()
                    navController.navigate("SignUpGoogleScreen"){
                        popUpTo("LoadingDataView") {inclusive = true}
                    }
                    Toast.makeText(context, "You must register first", Toast.LENGTH_LONG).show()
                }
            }
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