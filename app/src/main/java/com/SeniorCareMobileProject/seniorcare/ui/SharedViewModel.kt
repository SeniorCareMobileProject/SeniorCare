package com.SeniorCareMobileProject.seniorcare.ui

import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.BatteryManager
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.data.CalendarEvent
import com.SeniorCareMobileProject.seniorcare.data.LocalSettingsRepository
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.*
import com.SeniorCareMobileProject.seniorcare.data.emptyEvent
import com.SeniorCareMobileProject.seniorcare.data.util.LoadingState
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalDate
import org.koin.core.component.KoinComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList


class SharedViewModel() : ViewModel(), KoinComponent {

    lateinit var localSettingsRepository: LocalSettingsRepository

    // Bottom navigation bar
    val navBarIndex = mutableStateOf(0)

    var lastUpdateTime = LocalDateTime.now().toLocalTime().format(
        DateTimeFormatter.ofPattern("HH:mm"))

    var userFunctionFromLocalRepo = ""

    //location
    val onGeofenceRequest = MutableLiveData<Boolean>(false)
    val seniorLocalization = mutableStateOf(LatLng(52.408839, 16.906782))
    val localizationAccuracy = mutableStateOf(50f)
    val location = mutableStateOf<Location?>(null)
    val seniorLocalizationZoom = mutableStateOf(17f)
    val mapFullscreen = mutableStateOf(false)
    val resetCamera = mutableStateOf(false)
    val isHighAccuracyEnabled = MutableLiveData(true)
    val locationBeforeFreeingCam = mutableStateOf(LatLng(52.408839, 16.906782))

    //geofence
    val geofenceLocation = mutableStateOf(LatLng(1.0, 1.0))
    val geofenceRadius = mutableStateOf(1)
    val newGeofenceLocation = mutableStateOf(LatLng(1.0, 1.0))
    val newGeofenceRadius = mutableStateOf(20)


    // for getting input
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var isFunctionCarer = mutableStateOf(false)
    var isFunctionSenior = mutableStateOf(false)
    var function = mutableStateOf("")
    var emailForgotPassword = mutableStateOf("")

    // for checking if user is after logging in or after the register screen
    var isAfterRegistration: Boolean = false

    // for checking request state
    val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus
    val _userDataStatus = MutableLiveData<Resource<User>>()
    val userDataStatus: LiveData<Resource<User>> = _userDataStatus
    val isDataEmpty = MutableLiveData(false)
    val _currentSeniorDataStatus = MutableLiveData<Resource<User>>()
    val currentSeniorDataStatus: LiveData<Resource<User>> = _currentSeniorDataStatus
    val hasSeniorData: MutableLiveData<Boolean> = MutableLiveData(false)

    // user data
    val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData
    val isNewUser: MutableLiveData<Boolean> = MutableLiveData(false)
    val functionLiveData: MutableLiveData<String> = MutableLiveData(userData.value?.function)
    val listOfAllConnectedUsersID = mutableListOf<String>()
    val currentSeniorData: MutableLiveData<User> = MutableLiveData()

    val listOfConnectedUsers = mutableListOf<String>()
    var currentSeniorIndex = 0
    var haveConnectedUsers = true

    // for pairing users
    val pairingCode: MutableLiveData<String?> = MutableLiveData("")
    val pairingData: MutableLiveData<PairingData> = MutableLiveData()
    val pairingStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val pairingSeniorID: MutableLiveData<String> = MutableLiveData("")
    val writeNewConnectionStatus = MutableLiveData<Resource<String>>()

    // senior
    var codeInput = mutableStateOf("")
    val pairingDataStatus = MutableLiveData<Resource<PairingData>>()

    // sos button
    val sosCascadeIndex: MutableLiveData<Int> = MutableLiveData(-2)
    var sosCascadePhoneNumbers = mutableListOf<String>()
    var sosPhoneNumbersNames = mutableListOf<String>()
    var sosSettingsNumberStates = mutableListOf<MutableState<String>>()
    var sosSettingsNameStates = mutableListOf<MutableState<String>>()
    val sosCascadeInterval:Long = 10000
    val sosCascadeTimer = object : CountDownTimer(sosCascadeInterval, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            sosCascadeIndex.value = sosCascadeIndex.value!!.plus(1)
        }
    }

    // MEDICAL INFORMATION
    val medInfo: MutableLiveData<MedInfoDAO> = MutableLiveData()

    // Fall Detector
    var isFallDetectorTurnOn: MutableLiveData<Boolean> = MutableLiveData(false)

    //Calendar events
    //Stores data about new element
    var newEvent = emptyEvent.copy()
    //Stores original data about element that is being changed
    var modifiedEvent = emptyEvent.copy()
    var duringAddingOrEditing = mutableStateOf(false)
    var createNewEvent = mutableStateOf(false)
    var updateEvent = mutableStateOf(false)
    var removeEvent = mutableStateOf(false)
    var calendarEvents: MutableList<CalendarEvent> = mutableListOf()
    val calendarEventsFirebase = arrayListOf<CalendarEventDAO>()
    val removeEventConfirmation = mutableStateOf(false)
    val cancelRemoveEventConfirmation = mutableStateOf(false)

    //NOTIFICATIONS
    var notificationItems: MutableList<NotificationItem> = mutableListOf() //List of notifications
    var notificationitemsLiveData: MutableLiveData<MutableList<NotificationItem>> = MutableLiveData(notificationItems) //Livedata of notifcations list
    var notificationItemsNumber = 0 //Variable used for checking if there is a new notification

    //Battery

    var batteryPct: MutableLiveData<Float> = MutableLiveData(100.0F)



    private val repository = Repository()

    fun loginUser(userEmailAddress: String, userLoginPassword: String) {
        _userSignUpStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            repository.loginUser(this@SharedViewModel, userEmailAddress, userLoginPassword)
        }
    }

    fun registerUser(
        sharedViewModel: SharedViewModel,
        userEmailAddress: String,
        userLoginPassword: String,
        userFirstName: String,
        userLastName: String,
        userFunction: String
    ) {
        _userSignUpStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            repository.registerUser(
                this@SharedViewModel,
                userEmailAddress,
                userLoginPassword,
                userFirstName,
                userLastName,
                userFunction
            )
        }
    }

    fun getUserData() {
        viewModelScope.launch(Dispatchers.Main) {
            _userDataStatus.postValue(Resource.Loading())
            repository.getUserData(this@SharedViewModel)
        }
    }

    fun getCurrentSeniorData() {
        viewModelScope.launch(Dispatchers.Main) {
            _currentSeniorDataStatus.postValue(Resource.Loading())
            repository.getListOfSeniors(this@SharedViewModel)
        }
    }

    fun getSeniorIDForPairing() {
        repository.getSeniorIDForPairing(this)
    }

    fun createPairingCode() {
        repository.createPairingCodeAndWriteToFirebase(this)
    }

    fun deletePairingCode() {
        repository.removePairingCode(this)
    }

    fun getPairingData() {
        viewModelScope.launch(Dispatchers.Main) {
            pairingDataStatus.postValue(Resource.Loading())
            repository.getPairingData(this@SharedViewModel)
        }
    }

    fun writeSeniorIDForPairing() {
        repository.writeSeniorIDForPairing(this)
    }

    fun writeNewConnectionWith(carerID: String) {
        repository.writeNewConnectedWith(FirebaseAuth.getInstance().currentUser!!.uid, carerID)
    }

    fun updatePairingStatus() {
        repository.updatePairingStatus(this)
    }

    // GOOGLE SIGN IN
    fun userDataFromGoogle(email: String, displayName: String) {
        val fullName = displayName.split(" ")
        val firstName = fullName[0]
        val lastName = fullName[1]
        _userData.value = User(email, firstName, lastName, this.function.value)
    }

    fun writeNewUserFromGoogle(userData: LiveData<User>) {
        repository.writeNewUserFromGoogle(userData)
    }

    val loadingGoogleSignInState = MutableStateFlow(LoadingState.IDLE)
    val loadingGoogleLogInState = MutableStateFlow(LoadingState.IDLE)

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingGoogleSignInState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithCredential(credential).addOnCompleteListener{
                signInTask -> if(signInTask.isSuccessful){
                    isNewUser.value = signInTask.result.additionalUserInfo?.isNewUser
                }
            }.await()
            loadingGoogleSignInState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingGoogleSignInState.emit(LoadingState.error(e.localizedMessage))
        }
    }

    fun loginWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingGoogleLogInState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithCredential(credential).await()
            loadingGoogleLogInState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingGoogleLogInState.emit(LoadingState.error(e.localizedMessage))
        }
    }

    // LOCATION
    fun saveLocationToFirebase() {
        val locationAll = this@SharedViewModel.location.value
        val location =
            LocationDAO(locationAll?.latitude, locationAll?.longitude, locationAll?.accuracy)
        repository.saveLocationToFirebase(location)
    }

    // GEOFENCE
    fun saveGeofenceToFirebase(geoFenceLocation: GeofenceDAO) {
        repository.saveGeofenceToFirebase(geoFenceLocation, this)
    }

    fun getGeofenceForSenior() {
        repository.getGeofenceForSenior(this)
    }

    // MEDICAL INFO
    fun saveMedicalInfoToFirebase(){
        repository.saveMedicalInfo(this)
    }

    fun getMedicalInformationForSenior() {
        repository.getMedicalInformationForSenior(this)
    }

    // SOS - FIREBASE
    fun getSosNumbersForSenior() {
        repository.getSosNumbersForSenior(this)
    }

    fun saveSosNumbersToFirebase() {
        repository.saveSosNumbersToFirebase(this)
    }

    // LOCAL REPOSITORY
    fun getUserFunctionFromLocalRepo() {
        userFunctionFromLocalRepo = localSettingsRepository.readUserFunction().toString()
    }

    fun saveUserFunctionToLocalRepo(userFunction: String) {
        localSettingsRepository.saveUserFunction(userFunction)
    }

    fun getSosNumbersFromLocalRepo() {
        val allNumbersString = localSettingsRepository.readSosNumbers()
        val numbersToList = allNumbersString?.split(",")?.map { it.trim() }
        numbersToList?.forEach {
            sosCascadePhoneNumbers.add(it)
        }
    }

    fun saveSosNumbersToLocalRepo() {
        localSettingsRepository.saveSosNumbers(sosCascadePhoneNumbers.joinToString())
        Log.d("saveSosNumbersToLocalRepo", "Saved ${sosCascadePhoneNumbers.joinToString()}")
    }

    fun getFallDetectionStateFromLocalRepo() {
        isFallDetectorTurnOn.value = localSettingsRepository.readFallDetectionState()
    }

    fun saveFallDetectionStateToLocalRepo() {
        localSettingsRepository.saveFallDetectionState(isFallDetectorTurnOn.value!!)
        Log.d("saveFallDetectionStateToLocalRepo", "Saved $isFallDetectorTurnOn")
    }

    fun clearLocalRepository() {
        localSettingsRepository.clearRepository()
    }

    fun createShareMedInfoIntent(): Intent? {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, medInfo.value.toString())
            type = "text/plain"
        }
        return Intent.createChooser(sendIntent, "Udostępnij dane medyczne")
    }

    // CALENDAR EVENTS - FIREBASE
    fun saveCalendarEventsToFirebase() {
        parseCalendarEventsToCalendarEventsFirebase()
        repository.saveCalendarEvents(this)
    }

    fun loadCalendarEventsFromFirebase() {
        repository.loadCalendarEventsForSenior(this)
    }

    private fun parseCalendarEventsToCalendarEventsFirebase(){
        calendarEventsFirebase.clear()
        for (calendarEvent in calendarEvents){
            val calendarEventFirebase = CalendarEventDAO(
                calendarEvent.date.toString(),
                calendarEvent.startTime.toString(),
                calendarEvent.endTime.toString(),
                calendarEvent.eventName,
                calendarEvent.eventDescription
            )
            calendarEventsFirebase.add(calendarEventFirebase)
        }
    }

    fun parseCalendarEventsFirebaseToCalendarEvents(){
        calendarEvents.clear()
        for (calendarEventFirebase in calendarEventsFirebase){
            val calendarEvent = CalendarEvent(
                LocalDate.parse(calendarEventFirebase.date),
                LocalTime.parse(calendarEventFirebase.startTime),
                LocalTime.parse(calendarEventFirebase.endTime),
                calendarEventFirebase.eventName,
                calendarEventFirebase.eventDescription
            )
            calendarEvents.add(calendarEvent)
        }
    }

    // NOTIFICATIONS - firebase
    fun saveNotificationsToFirebase() {
        repository.saveNotificationsToFirebase(this)
    }

    fun getNotificationsForSenior() {
        viewModelScope.launch {
            repository.getNotificationsForSenior().collectLatest {
                notificationItems.clear()
                for (item in it){
                    notificationItems.add(item)
                }
                notificationitemsLiveData.value = notificationItems
            }
        }
    }

    fun getListOfCarers() {
        repository.getListOfCarers(this)
    }

    fun clearVariablesToChangeSenior() {
        seniorLocalization.value = LatLng(52.237049, 21.017532)
        localizationAccuracy.value = 50f
        geofenceLocation.value = LatLng(1.0, 1.0)
        geofenceRadius.value = 1
        medInfo.value = MedInfoDAO()
        sosCascadePhoneNumbers.clear()
        sosPhoneNumbersNames.clear()
        sosSettingsNumberStates.clear()
        sosSettingsNameStates.clear()
        calendarEvents.clear()
        calendarEventsFirebase.clear()
        notificationItems.clear()
        notificationitemsLiveData.value = notificationItems
    }


    fun disconnectWithSenior() {
        repository.disconnectUsers(this)
        listOfAllConnectedUsersID.removeAt(currentSeniorIndex)
        if (listOfAllConnectedUsersID.isEmpty()) {
            haveConnectedUsers = false
        }
        currentSeniorIndex = 0
    }

    fun saveBatteryInfoToFirebase(){
        repository.saveBatteryInfoToFirebase()
    }

    fun getBatteryInfo() {
        viewModelScope.launch {
            repository.getBatteryInfoFromAllSeniors().collectLatest {
                Log.d("All Seniors with battery info:", it.toString())
            }
        }
    }
}