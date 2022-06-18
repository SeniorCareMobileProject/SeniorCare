package com.SeniorCareMobileProject.seniorcare

import android.Manifest
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.receivers.GeofenceBroadcastReceiver
import com.SeniorCareMobileProject.seniorcare.services.CurrentLocationService
import com.SeniorCareMobileProject.seniorcare.services.LocationJobScheduler
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.navigation.BottomNavItem
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.*
import com.SeniorCareMobileProject.seniorcare.utils.SharedPreferenceUtil
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private var locations: Location? = null

class MainActivity : ComponentActivity() {

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(MyApplication.context, GeofenceBroadcastReceiver::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                MyApplication.context,
                0,
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                MyApplication.context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private val requestCall = 1

    private val sharedViewModel: SharedViewModel by viewModels()
    private var foregroundOnlyLocationServiceBound = false
    private var currentOnlyLocationService: CurrentLocationService? = null
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private val foregroundOnlyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as CurrentLocationService.LocalBinder
            currentOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
            currentOnlyLocationService?.subscribeToLocationUpdates()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            currentOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    lateinit var geofencingClient: GeofencingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleJob()

        sharedViewModel.onGeofenceRequest.observe(this, Observer { value ->
            if (value) handleGeofence()
        })

        geofencingClient = LocationServices.getGeofencingClient(this)
        geofencePendingIntent.send()

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val disabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )

        sharedViewModel.userData.observe(this, Observer { value ->
            if (value.function == "Senior") {
                if (disabled) {
                    currentOnlyLocationService?.unSubscribeToLocationUpdates()
                } else {
                    if (foregroundPermissionApproved()) {
                        currentOnlyLocationService?.subscribeToLocationUpdates()
                            ?: Log.d("TAG", "Service Not Bound")
                    } else {
                        requestForegroundPermissions()
                    }
                }
            }
        })
        sharedViewModel.sosCascadeIndex.observe(this, Observer{
            value -> if(value >=0) {
            makePhoneCall(sharedViewModel.sosCascadePhoneNumbers[sharedViewModel.sosCascadeIndex.value!!])
            if(sharedViewModel.sosCascadeIndex.value!!>=sharedViewModel.sosCascadePhoneNumbers.size-1){
                sharedViewModel.sosCascadeIndex.value = -1
            }
        }
        }
        )

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser?.uid
        var startDestination = ""
        if (currentUser != null) {
            sharedViewModel.getUserData()
            startDestination = NavigationScreens.LoadingDataView.name
        } else {
            startDestination = NavigationScreens.ChooseLoginMethodScreen.name
        }


        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

                val items = listOf(
                    listOf("Imię", "Grzegorz"),
                    listOf("Nazwisko", "Brzęczyszczykiewicz"),
                    listOf("Data urodzenia", "17.06.1943 (79 lat)"),
                    listOf("Choroby", "Demencja"),
                    listOf("Grupa krwi", "A+"),
                    listOf("Alergie", "Orzechy"),
                    listOf(
                        "Przyjmowane leki",
                        "Donepezil (50mg dwa razy dziennie)\n" + "Galantamin (25mg trzy razy dziennie)"
                    ),
                    listOf("Wzrost", "168"),
                    listOf("Waga", "58"),
                    listOf("Główny język", "Polski"),
                    listOf("Inne", "Inne informacje/uwagi o podopiecznym"),
                )

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
                        CarerMedicalInfoView(navController, scope, scaffoldState)
                    }
                    composable(BottomNavItem.Notifications.route) {
                        CarerNotificationsView(navController, sharedViewModel, scope, scaffoldState)
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

                    composable(NavigationScreens.CarerPairingScreen.name) {
                        CarerPairingView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorCalendarScreen.name) {
                        SeniorCalendarView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMainScreen.name) {
                        SeniorMainView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorMedicalInfoScreen.name) {
                        SeniorMedicalInfoView(navController)

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
                            scope,
                            scaffoldState,
                            items
                        )
                    }
                    composable(NavigationScreens.SeniorSettingsScreen.name) {
                        SeniorSettingsView(navController)

                    }

                    composable(NavigationScreens.SosCascadeView.name) {
                        SosCascadeView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerSettingsListScreen.name) {
                        CarerSettingsListView(navController)

                    }

                    composable(NavigationScreens.CarerSettingsSOSScreen.name) {
                        CarerSettingsSOSView(navController)

                    }

                    composable(NavigationScreens.CarerSettingsSOSUpdateScreen.name) {
                        CarerSettingsSOSUpdateView(navController)

                    }

                    composable(NavigationScreens.SeniorGoingOutInfoScreen.name) {
                        SeniorGoingOutInfoView(navController)

                    }

                    composable(NavigationScreens.SettingsFallDetectorScreen.name) {
                        SettingsFallDetectorView(navController)

                    }

                    composable(NavigationScreens.SeniorCarersListScreen.name) {
                        SeniorCarersListView(navController)

                    }
                }
            }
        }
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

    fun scheduleJob() {
        val componentName = ComponentName(this, LocationJobScheduler::class.java)
        val info = JobInfo.Builder(123, componentName)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic((1 * 30 * 1000).toLong())
            .build()
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

    fun cancelJob() {
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(123)
        Log.d(TAG, "Job cancelled")
    }

    override fun onStart() {
        super.onStart()

        // sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this, CurrentLocationService::class.java)
        bindService(
            serviceIntent,
            foregroundOnlyServiceConnection,
            Context.BIND_AUTO_CREATE
        )


    }

    private fun handleGeofence() {
        Log.d("CreateGeofence", "Main")
        createGeoFence(sharedViewModel.seniorLocalization.value, geofencingClient)
        sharedViewModel.onGeofenceRequest.value = false
        sharedViewModel.geofenceLocation.value = sharedViewModel.seniorLocalization.value
        sharedViewModel.geofenceRadius.value = GEOFENCE_RADIUS
    }

    private fun createGeoFence(location: LatLng, geofencingClient: GeofencingClient) {
        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(location.latitude, location.longitude, GEOFENCE_RADIUS.toFloat())
            .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
            .addGeofence(geofence)
            .build()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    GEOFENCE_LOCATION_REQUEST_CODE
                )
            } else {
                geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
            }
        } else {
            geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)
        }

        geofencePendingIntent.send()

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                CurrentLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }


    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (currentOnlyLocationService != null) {
            currentOnlyLocationService?.unSubscribeToLocationUpdates()
        }

        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        // sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }


    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        if (provideRationale) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE
                ),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE
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

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    currentOnlyLocationService?.subscribeToLocationUpdates()
                else -> {

                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.VERSION_NAME,
                        null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                CurrentLocationService.EXTRA_LOCATION
            )
            Log.d(
                "CurrentLocationReceiver",
                "${locations?.latitude}, ${locations?.longitude}"
            )
            if (location != null) {
                locations = location
                sharedViewModel.seniorLocalization.value =
                    LatLng(location.latitude, location.longitude)
                sharedViewModel.localizationAccuracy.value = location.accuracy
                sharedViewModel.location.value = locations

                val firebaseAuth = FirebaseAuth.getInstance()
                val currentUser = firebaseAuth.currentUser?.uid
                if (currentUser != null) {
                    sharedViewModel.saveLocationToFirebase()
                }
            }
        }
    }
}


const val GEOFENCE_RADIUS = 200
const val GEOFENCE_ID = "SENIOR_GEOFENCE"
const val GEOFENCE_EXPIRATION = 10 * 24 * 60 * 60 * 1000 // 10 days
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345