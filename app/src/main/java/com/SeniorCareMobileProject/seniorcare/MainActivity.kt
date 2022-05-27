package com.SeniorCareMobileProject.seniorcare

import android.Manifest
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.*
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SeniorCalendarView
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SeniorMainView
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SeniorMedicalInfoView
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SeniorNotificationView
import com.SeniorCareMobileProject.seniorcare.ui.views.TemplateView


import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.net.Uri
import android.provider.Settings
import android.view.Surface
import androidx.compose.material.MaterialTheme
import com.SeniorCareMobileProject.seniorcare.BuildConfig
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
private var locations:Location?=null

class MainActivity : ComponentActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


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



        geofencingClient = LocationServices.getGeofencingClient(this)
        geofencePendingIntent.send()

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )
        if (enabled) {
            currentOnlyLocationService?.unSubscribeToLocationUpdates()
        } else {
            if (foregroundPermissionApproved()) {
                currentOnlyLocationService?.subscribeToLocationUpdates()
                    ?: Log.d("TAG", "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }

        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()

                NavHost(
                    navController,
                    startDestination = NavigationScreens.FirstStartUpScreen.name,
                ) {
                    composable(NavigationScreens.FirstStartUpScreen.name) {
                        FirstStartUpView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.LoginScreen.name) {
                        LoginView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.RegisterScreen.name) {
                        RegisterView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.ChooseRoleScreen.name) {
                        ChooseRoleView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.ForgotPasswordScreen.name) {
                        ForgotPasswordView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.RegisterCodeScreen.name) {
                        RegisterView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerCreatingNotificationsScreen.name) {
                        CarerCreatingNotificationsView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerDayPlanningScreen.name) {
                        CarerDayPlanningView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerMainScreen.name) {
                        CarerMainView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.CarerMedicalInfoScreen.name) {
                        CarerMedicalInfoView(navController, sharedViewModel)

                    }

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
                        SeniorMedicalInfoView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.SeniorNotificationScreen.name) {
                        SeniorNotificationView(navController, sharedViewModel)

                    }

                    composable(NavigationScreens.TemplateScreen.name) {
                        TemplateView(navController, sharedViewModel)
                    }

//                    composable(NavigationScreens.MapsTestScreen.name) {
//                        MapsTestView(navController, sharedViewModel)
//
//                    }

                }
            }
        }
    }

            override fun onStart() {
                super.onStart()

                updateButtonState(
                    sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
                )
                sharedPreferences.registerOnSharedPreferenceChangeListener(this)

                val serviceIntent = Intent(this, CurrentLocationService::class.java)
                bindService(
                    serviceIntent,
                    foregroundOnlyServiceConnection,
                    Context.BIND_AUTO_CREATE
                )
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
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

                super.onStop()
            }

            override fun onSharedPreferenceChanged(
                sharedPreferences: SharedPreferences,
                key: String
            ) {
                if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
                    updateButtonState(
                        sharedPreferences.getBoolean(
                            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                        )
                    )
                }
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
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
            }

            override fun onRequestPermissionsResult(
                requestCode: Int,
                permissions: Array<String>,
                grantResults: IntArray
            ) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                Log.d(TAG, "onRequestPermissionResult")

                when (requestCode) {
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                        grantResults.isEmpty() ->
                            Log.d(TAG, "User interaction was cancelled.")
                        grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                            currentOnlyLocationService?.subscribeToLocationUpdates()
                        else -> {
                            updateButtonState(false)
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

            private fun updateButtonState(trackingLocation: Boolean) {
                //Update the location here #trackingLocation
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
                        sharedViewModel.seniorLocalization.value = LatLng( location.latitude, location.longitude)
                        sharedViewModel.localizationAccuracy.value = location.accuracy
                        sharedViewModel.location.value = locations

                    }
                }
            }
        }


//
//private var locations:Location?=null
//private lateinit var  mapView: MapView
//
//@Composable
//fun GetCurrentLocation() {
//    mapView = rememberMapViewWithLifeCycle()
//
//    Log.d("Current Location", "${locations?.latitude}, ${locations?.longitude}")
//    val locState by remember { mutableStateOf(locations)}
//    Log.d("CurrentLocationlocState", "${locState?.latitude}, ${locState?.longitude}")
//
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        AndroidView(
//            {mapView}
//        ) { mapView ->
//            CoroutineScope(Dispatchers.Main).launch {
//                val map = mapView.awaitMap()
//                map.uiSettings.isZoomControlsEnabled = true
//                if (locations !=null){
//                    val destination = LatLng(locState!!.latitude, locState!!.longitude)
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 6f))
//                    val markerOptions = destination.let {
//                        MarkerOptions()
//                            .title("Your location")
//                            .position(it)
//                    }
//                    map.addMarker(markerOptions)
//                } else {
//                    val destination = LatLng(12.9716, 77.5946)
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 6f))
//                    val markerOptions =  MarkerOptions()
//                        .title("Static location")
//                        .position(destination)
//                    map.addMarker(markerOptions)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun rememberMapViewWithLifeCycle(): MapView {
//    val context = LocalContext.current
//    val mapView = remember {
//        MapView(context).apply {
//            id = com.google.maps.android.ktx.R.id.map_frame
//        }
//    }
//    val lifeCycleObserver = rememberMapLifecycleObserver(mapView)
//    val lifeCycle = LocalLifecycleOwner.current.lifecycle
//    DisposableEffect(lifeCycle) {
//        lifeCycle.addObserver(lifeCycleObserver)
//        onDispose {
//            lifeCycle.removeObserver(lifeCycleObserver)
//        }
//    }
//
//    return mapView
//}
//
//@Composable
//fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
//    remember(mapView) {
//        LifecycleEventObserver { _, event ->
//            when(event) {
//                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
//                Lifecycle.Event.ON_START -> mapView.onStart()
//                Lifecycle.Event.ON_RESUME -> mapView.onResume()
//                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
//                Lifecycle.Event.ON_STOP -> mapView.onStop()
//                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
//                else -> throw IllegalStateException()
//            }
//        }
//    }


//class MainActivity : ComponentActivity() {
//
//    private val sharedViewModel: SharedViewModel by viewModels()
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//
//            setContent {
//                SeniorCareTheme() {
//                    val navController = rememberNavController()
//
//                    NavHost(
//                        navController,
//                        startDestination = NavigationScreens.FirstStartUpScreen.name,
//                    ) {
//                        composable(NavigationScreens.FirstStartUpScreen.name) {
//                            FirstStartUpView(navController, sharedViewModel)
//                        }
//
//                        composable(NavigationScreens.LoginScreen.name) {
//                            LoginView(navController, sharedViewModel)
//                        }
//
//                        composable(NavigationScreens.RegisterScreen.name) {
//                            RegisterView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.ChooseRoleScreen.name) {
//                            ChooseRoleView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.ForgotPasswordScreen.name) {
//                            ForgotPasswordView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.RegisterCodeScreen.name) {
//                            RegisterView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.CarerCreatingNotificationsScreen.name) {
//                            CarerCreatingNotificationsView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.CarerDayPlanningScreen.name) {
//                            CarerDayPlanningView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.CarerMainScreen.name) {
//                            CarerMainView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.CarerMedicalInfoScreen.name) {
//                            CarerMedicalInfoView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.CarerPairingScreen.name) {
//                            CarerPairingView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.SeniorCalendarScreen.name) {
//                            SeniorCalendarView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.SeniorMainScreen.name) {
//                            SeniorMainView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.SeniorMedicalInfoScreen.name) {
//                            SeniorMedicalInfoView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.SeniorNotificationScreen.name) {
//                            SeniorNotificationView(navController, sharedViewModel)
//
//                        }
//
//                        composable(NavigationScreens.TemplateScreen.name) {
//                            TemplateView(navController, sharedViewModel)
//                        }
//
////                    composable(NavigationScreens.MapsTestScreen.name) {
////                        MapsTestView(navController, sharedViewModel)
////
////                    }
//
//                    }
//
//                }
//            }
//    }
//}
