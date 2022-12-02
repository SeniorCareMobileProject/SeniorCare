package com.SeniorCareMobileProject.seniorcare

import android.Manifest
import android.app.*
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.data.LocalSettingsRepository
import com.SeniorCareMobileProject.seniorcare.data.dao.GeofenceDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.MedInfoDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.fallDetector.FallDetectorService
import com.SeniorCareMobileProject.seniorcare.receivers.GeofenceBroadcastReceiver
import com.SeniorCareMobileProject.seniorcare.receivers.NotificationsBroadcastReceiver
import com.SeniorCareMobileProject.seniorcare.services.LocationJobScheduler
import com.SeniorCareMobileProject.seniorcare.services.LocationService
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.MapWindowComponent
import com.SeniorCareMobileProject.seniorcare.ui.common.MapsAddGeofenceComponent
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
import java.util.*

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


    lateinit var geofencingClient: GeofencingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localSettingsRepository = LocalSettingsRepository.getInstance(application)
        sharedViewModel.localSettingsRepository = localSettingsRepository

        scheduleJob()
     //   startForegroundService(Intent(this, MainForegroundService::class.java))


        sharedViewModel.onGeofenceRequest.observe(this, Observer { value ->
            if (value) handleGeofence()
        })

        sharedViewModel.hasSeniorData.observe(this, Observer { value ->
            if (value){
                createGeoFence(sharedViewModel.geofenceLocation.value,sharedViewModel.geofenceRadius.value, geofencingClient)
                sharedViewModel.hasSeniorData.value = false
                Log.d("Create GeoFence", "Utworzono geofence")
            }
        })


        geofencingClient = LocationServices.getGeofencingClient(this)
        geofencePendingIntent.send()



        //TODO : na potrzeby testowania geofencingu i map, odkomentowanie poniższego kodu sprawi że trackujemy lokalizacje opiekuna a nie seniora
////todo delete \/
//        if (foregroundPermissionApproved()) {
//            currentOnlyLocationService?.subscribeToLocationUpdates()
//                ?: Log.d("TAG", "Service Not Bound")
//        } else {
//            requestForegroundPermissions()
//        }
////todo delete /\

        sharedViewModel.getUserFunctionFromLocalRepo()
        if (sharedViewModel.userFunctionFromLocalRepo == "Senior"){
            sharedViewModel.getSosNumbersFromLocalRepo()
            sharedViewModel.getFallDetectionStateFromLocalRepo()
            Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)}
            if (sharedViewModel.isFallDetectorTurnOn.value == true) {
                val fallDetectorService = FallDetectorService()
                val fallDetectorServiceIntent = Intent(this, fallDetectorService.javaClass)
                startService(fallDetectorServiceIntent)
            }
        }

        requestForegroundPermissions()

        sharedViewModel.sosCascadeIndex.observe(this, Observer { value ->
            if (value >= 0) {
                makePhoneCall(sharedViewModel.sosCascadePhoneNumbers[sharedViewModel.sosCascadeIndex.value!!])
                if (sharedViewModel.sosCascadeIndex.value!! >= sharedViewModel.sosCascadePhoneNumbers.size - 1) {
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

        sharedViewModel.notificationitemsLiveData.observe(this) { value ->
            if (currentUser != null) {
                if (sharedViewModel.userData.value?.function == "Senior"){
                    if (value.size != sharedViewModel.notificationItemsNumber) {
                        sharedViewModel.notificationItemsNumber = value.size
                        cancelAllAlarms()
                        scheduleNotifications()
                    }

                }                }

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

                    composable(NavigationScreens.CarerPairingScreen.name) {
                        CarerPairingView(navController, sharedViewModel)

                    }

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
                        SeniorGoingOutInfoView(navController)

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

                }
            }
        }
    }

    private fun setMedInfoToNotBeNull(){
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

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun manageNotificationChannel(context: Context?, id: String) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, "channel_name", importance)
            channel.description = "channel desc"

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            val a : NotificationManager? = null
            val notificationManager = NotificationManagerCompat.from(context!!)
            notificationManager.createNotificationChannel(channel)
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

    fun scheduleNotifications(){
        Log.e(TAG, "onStartCommand")
        Log.e(TAG,sharedViewModel.notificationItems.size.toString())
        Log.e(TAG,sharedViewModel.notificationitemsLiveData.value.toString())
        //startTimer()

        for(i in 0 until sharedViewModel.notificationItems.size){
            for(j in 0 until sharedViewModel.notificationItems[i].timeList.size){
                Log.e(TAG,sharedViewModel.notificationItems[i].name)


                Log.e(TAG, Integer.parseInt(sharedViewModel.notificationItems[i].timeList[j].subSequence(0,2).toString()).toString() + " " + Integer.parseInt(sharedViewModel.notificationItems[i].timeList[j].subSequence(3,5).toString()).toString() )
                setAlarm(
                    Integer.parseInt(sharedViewModel.notificationItems[i].timeList[j].subSequence(0,2).toString()),
                    Integer.parseInt(sharedViewModel.notificationItems[i].timeList[j].subSequence(3,5).toString()),
                    sharedViewModel.notificationItems[i].interval,
                    i,
                    j
                )
            }

        }
    }

    fun setAlarm(hour: Int, minute: Int, interval: String, notificationId: Int, timeId: Int){
        manageNotificationChannel(context, notificationId.toString())
        val alarmManager = context?.getSystemService(ALARM_SERVICE) as AlarmManager
        Log.e(TAG,sharedViewModel.notificationItems[notificationId].name)
        val bundle = Bundle()
            bundle.putInt("NotificationId",notificationId)
            bundle.putString("Title",sharedViewModel.notificationItems[notificationId].name)
            bundle.putInt("TimeId",timeId)
        val intent = Intent(context, NotificationsBroadcastReceiver::class.java)
        intent.putExtras(bundle)

        val pendingIntent = PendingIntent.getBroadcast(context, notificationId*3 + timeId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var alarmInterval = AlarmManager.INTERVAL_DAY
        if(interval.equals("Co 2 dni")){
            alarmInterval *= 2
        }
        if(interval.equals("Co tydzień")){
            alarmInterval *= 7
        }

        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= hour && get(Calendar.MINUTE)>=minute) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmInterval,
            pendingIntent
        )
    }

    fun cancelAllAlarms(){
        for(i in 0 until sharedViewModel.notificationItems.size){
            for(j in 0 until sharedViewModel.notificationItems[i].timeList.size){

                cancelAlarm(
                    context,
                    i,
                    j
                )
            }

        }
    }

    fun cancelAlarm(context: Context?, notificationId: Int, timeId: Int){

        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.cancel(notificationId*3+timeId)
        notificationManager.deleteNotificationChannel("$notificationId")
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


    private fun handleGeofence() {
        Log.d("CreateGeofence", "Main")
        sharedViewModel.onGeofenceRequest.value = false
        sharedViewModel.geofenceLocation.value = sharedViewModel.newGeofenceLocation.value
        sharedViewModel.geofenceRadius.value = sharedViewModel.newGeofenceRadius.value

        // Zapis Geofence do firebase pod adres seniora
        val geoFenceLocation = GeofenceDAO(sharedViewModel.geofenceLocation.value.latitude, sharedViewModel.geofenceLocation.value.longitude, sharedViewModel.geofenceRadius.value)
        sharedViewModel.saveGeofenceToFirebase(geoFenceLocation)
        sharedViewModel.deleteShowAlarm()
        sharedViewModel.listenToGeofenceStatus()
    }

    private fun createGeoFence(location: LatLng, radius: Int, geofencingClient: GeofencingClient) {
        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(location.latitude, location.longitude, radius.toFloat())
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


    override fun onDestroy(){
        super.onDestroy()
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

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    Log.d(TAG, "User interaction was cancelled.")
               // grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                   // if (sharedViewModel.userData.value != null )
                   // currentOnlyLocationService?.subscribeToLocationUpdates()
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

}


const val GEOFENCE_ID = "SENIOR_GEOFENCE"
const val GEOFENCE_EXPIRATION = 10 * 24 * 60 * 60 * 1000 // 10 days
const val GEOFENCE_LOCATION_REQUEST_CODE = 12345