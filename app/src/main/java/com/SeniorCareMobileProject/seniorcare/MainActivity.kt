package com.SeniorCareMobileProject.seniorcare

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.data.LocalSettingsRepository
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.GeofenceDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.MedInfoDAO
import com.SeniorCareMobileProject.seniorcare.fallDetector.FallDetectorService
import com.SeniorCareMobileProject.seniorcare.receivers.GeofenceBroadcastReceiver
import com.SeniorCareMobileProject.seniorcare.receivers.NotificationsBroadcastReceiver
import com.SeniorCareMobileProject.seniorcare.services.CarerService
import com.SeniorCareMobileProject.seniorcare.services.SeniorService
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.common.MapsAddGeofenceComponent
import com.SeniorCareMobileProject.seniorcare.ui.navigation.BottomNavItem
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.*
import com.SeniorCareMobileProject.seniorcare.utils.HighAccuracyLocation
import com.google.firebase.auth.FirebaseAuth
import java.util.*

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class MainActivity : ComponentActivity() {

    private val requestCall = 1
    private var seniorIndex = 0
    private var seniorId = "0"
    private var function: String = "Carer"

    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localSettingsRepository = LocalSettingsRepository.getInstance(application)
        sharedViewModel.localSettingsRepository = localSettingsRepository


        sharedViewModel.onGeofenceRequest.observe(this, Observer { value ->
            if (value) handleGeofence()
        })


//        requestForegroundPermissions()

        sharedViewModel.getUserFunctionFromLocalRepo()
        sharedViewModel.hasUserFunction.observeOnce(this, Observer<Boolean?>{
            sharedViewModel.getUserFunctionFromLocalRepo()
            if (sharedViewModel.userFunctionFromLocalRepo == "Senior") {
                function = "Senior"
                sharedViewModel.getSosNumbersFromLocalRepo()
                sharedViewModel.getFallDetectionStateFromLocalRepo()
                HighAccuracyLocation().askForHighAccuracy(this)
                Intent(applicationContext, SeniorService::class.java).apply {
                    action = SeniorService.ACTION_START
                    startService(this)
                }
                if (sharedViewModel.isFallDetectorTurnOn.value == true) {
                    val fallDetectorService = FallDetectorService()
                    val fallDetectorServiceIntent = Intent(this, fallDetectorService.javaClass)
                    startService(fallDetectorServiceIntent)
                }
            }
            if (sharedViewModel.userFunctionFromLocalRepo == "Carer") {
                Intent(applicationContext, CarerService::class.java).apply {
                    action = CarerService.ACTION_START
                    startService(this)
                }
            }

        })



        sharedViewModel.sosCascadeIndex.observe(this, Observer { value ->
            if (value >= 0) {
                makePhoneCall(sharedViewModel.sosCascadePhoneNumbers[sharedViewModel.sosCascadeIndex.value!!])
                if (sharedViewModel.sosCascadeIndex.value!! >= sharedViewModel.sosCascadePhoneNumbers.size - 1) {
                    sharedViewModel.sosCascadeIndex.value = -1
                }
            }
        }
        )
        sharedViewModel.hasListOfConnectedUsers.observe(this, Observer {
            Log.d("SENIOR DATA", "INIT")
            if (sharedViewModel.userFunctionFromLocalRepo == "Carer") {
                if (it == true) {
                    Log.d("SENIOR DATA", "SAVE TRUE")
                    seniorId =
                        sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex]
                    Repository().saveTrackingSettingsCarer(true, seniorId)
                }
                if (seniorIndex != sharedViewModel.currentSeniorIndex) {
                    Log.d("SENIOR DATA", "SAVE FALSE")
                    Repository().saveTrackingSettingsCarer(false, seniorId)
                    seniorId =
                        sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex]
                    seniorIndex = sharedViewModel.currentSeniorIndex
                }
            }
        })


        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser?.uid
        var startDestination = ""

        if (!foregroundPermissionApproved()){
            startDestination = NavigationScreens.PermissionInfoScreen.name
        } else {
            if (currentUser != null) {
                sharedViewModel.getUserData()
                startDestination = NavigationScreens.LoadingDataView.name
            } else {
                startDestination = NavigationScreens.ChooseLoginMethodScreen.name
            }
        }

        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                setMedInfoToNotBeNull()

                NavHost(
                    navController,
                    startDestination = startDestination,
                ) {


                    composable(BottomNavItem.Location.route) {
                        CarerMainView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.Calendar.route) {
                        CarerCalendarView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.MedInfo.route) {
                        CarerMedicalInfoView(navController, sharedViewModel, scope, scaffoldState)
                    }
                    composable(BottomNavItem.Notifications.route) {
                        CarerNotificationsView(navController, sharedViewModel, scope, scaffoldState)
                    }

                    composable(NavigationScreens.PermissionInfoScreen.name) {
                        PermissionInfoScreen(this@MainActivity, navController)
                    }

                    composable(NavigationScreens.ChooseLoginMethodScreen.name) {
                        ChooseLoginMethodView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.LoginScreen.name) {
                        LoginView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.LoadingSeniorDataView.name) {
                        LoadingSeniorDataView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.MapsAddGeofenceComponent.name) {
                        MapsAddGeofenceComponent(sharedViewModel)
                    }

//                    composable(NavigationScreens.ChooseRoleScreen.name) {
//                        ChooseRoleView(navController, sharedViewModel)
//
//                    }
//
//                    composable(NavigationScreens.ForgotPasswordScreen.name) {
//                        ForgotPasswordView(navController, sharedViewModel)
//
//                    }

                    composable(NavigationScreens.MapComponentView.name) {
                        MapWindowComponent(sharedViewModel)

                    }

                    composable(NavigationScreens.LoadingLoginView.name) {
                        LoadingLoginView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.LoadingRegisterView.name) {
                        LoadingRegisterView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.LoadingDataView.name) {
                        LoadingDataView(navController, sharedViewModel)

                    }


//                    composable(NavigationScreens.CarerCreatingNotificationsScreen.name) {
//                        CarerCreatingNotificationsView(navController, sharedViewModel)
//
//                    }

//                    composable(NavigationScreens.CarerDayPlanningScreen.name) {
//                        CarerCalendarView(navController, sharedViewModel)
//
//                    }
//
//
//                    composable(NavigationScreens.CarerMainScreen.name) {
//                        CarerMainView(navController, sharedViewModel)
//
//                    }
//
//                    composable(NavigationScreens.CarerMedicalInfoScreen.name) {
//                        CarerMedicalInfoView(navController, sharedViewModel)
//
//                    }

//                    composable(NavigationScreens.CarerPairingScreen.name) {
//                        CarerPairingView(navController, sharedViewModel)
//
//                    }

                    composable(NavigationScreens.SeniorCalendarScreen.name) {
                        SeniorCalendarScreenView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMainScreen.name) {
                        SeniorMainView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMedicalInfoScreen.name) {
                        SeniorMedicalInfoView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorNotificationScreen.name) {
                        SeniorNotificationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpGoogleScreen.name) {
                        SignUpGoogleView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpEmailScreen.name) {
                        SignUpEmailView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SignUpEmailVerificationScreen.name) {
                        SignUpEmailVerificationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PasswordRecoveryScreen.name) {
                        PasswordRecoveryView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PasswordRecoveryEmailScreen.name) {
                        PasswordRecoveryEmailView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenCodeScreen.name) {
                        PairingScreenCodeView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenSuccessScreen.name) {
                        PairingScreenSuccessView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenCodeInputScreen.name) {
                        PairingScreenCodeInputView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenConfirmationScreen.name) {
                        PairingScreenConfirmationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.PairingScreenSeniorSuccessScreen.name) {
                        PairingScreenSeniorSuccessView(navController, sharedViewModel)

                    }


                    composable(NavigationScreens.LoadingPairingDataView.name) {
                        LoadingPairingDataView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerMedicalInfoDataUpdateScreen.name) {
                        CarerMedicalInfoDataUpdateView(
                            navController,
                            sharedViewModel,
                            scope,
                            scaffoldState
                        )
                    }
                    composable(NavigationScreens.SeniorSettingsScreen.name) {
                        SeniorSettingsView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SosCascadeView.name) {
                        SosCascadeView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerSettingsListScreen.name) {
                        CarerSettingsListView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerSettingsSOSScreen.name) {
                        CarerSettingsSOSView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerSettingsSOSUpdateScreen.name) {
                        CarerSettingsSOSUpdateView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorGoingOutInfoScreen.name) {
                        SeniorGoingOutInfoView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SettingsFallDetectorScreen.name) {
                        SettingsFallDetectorView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorCarersListScreen.name) {
                        SeniorCarersListView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerSettingsSafeZoneScreen.name) {
                        CarerSettingsSafeZoneView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerNoConnectedSeniorsView.name) {
                        CarerNoConnectedSeniorsView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.CarerAboutAppView.name) {
                        CarerAboutAppView(navController, sharedViewModel, scope, scaffoldState)
                    }

                }
            }
        }
    }

    private fun setMedInfoToNotBeNull() {
        sharedViewModel.medInfo.value = MedInfoDAO(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        )
    }


    fun makePhoneCall(number: String) {
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val phoneNumber = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber)))
            }
        } else {
            Toast.makeText(
                this@MainActivity,
                "Nie podano numeru telefonu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun handleGeofence() {
        Log.d("CreateGeofence", "Main")
        sharedViewModel.onGeofenceRequest.value = false
        sharedViewModel.geofenceLocation.value = sharedViewModel.newGeofenceLocation.value
        sharedViewModel.geofenceRadius.value = sharedViewModel.newGeofenceRadius.value

        // Zapis Geofence do firebase pod adres seniora
        val geoFenceLocation = GeofenceDAO(
            sharedViewModel.geofenceLocation.value.latitude,
            sharedViewModel.geofenceLocation.value.longitude,
            sharedViewModel.geofenceRadius.value
        )
        sharedViewModel.saveGeofenceToFirebase(geoFenceLocation)
    }


    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        if (provideRationale) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS
                ),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS
                ),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall("123456789")
            } else {
                Toast.makeText(this, "Odmowa dostępu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        if (sharedViewModel.userFunctionFromLocalRepo == "Carer") {
            Log.d("SENIOR DATA", "SAVE TRUE")
            seniorId =
                sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex]
            Repository().saveTrackingSettingsCarer(false, seniorId)
        }
        super.onDestroy()
    }

}


fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}