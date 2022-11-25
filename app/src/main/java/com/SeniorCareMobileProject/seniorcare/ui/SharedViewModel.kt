package com.SeniorCareMobileProject.seniorcare.ui

import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.SeniorCareMobileProject.seniorcare.data.CalendarEvent
import com.SeniorCareMobileProject.seniorcare.data.LocalSettingsRepository
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.*
import com.SeniorCareMobileProject.seniorcare.data.emptyEvent
import com.SeniorCareMobileProject.seniorcare.data.util.LoadingState
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.NotificationItem
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.core.component.KoinComponent


class SharedViewModel() : ViewModel(), KoinComponent {

    lateinit var localSettingsRepository: LocalSettingsRepository

    // Bottom navigation bar
    val navBarIndex = mutableStateOf(0)

    var userFunctionFromLocalRepo = ""

    //location
    val onGeofenceRequest = MutableLiveData<Boolean>(false)
    val seniorLocalization = mutableStateOf(LatLng(52.408839, 16.906782))
    val localizationAccuracy = mutableStateOf(50f)
    val location = mutableStateOf<Location?>(null)
    val seniorLocalizationZoom = mutableStateOf(17f)
    val mapFullscreen = mutableStateOf(false)
    val resetCamera = mutableStateOf(false)
    val locationBeforeFreeingCam = mutableStateOf(LatLng(52.408839, 16.906782))

    val onNotficationShow = MutableLiveData<String>("false")

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
    val listOfAllSeniors = mutableListOf<String>()
    val currentSeniorData: MutableLiveData<User> = MutableLiveData()

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
    var calendarEvents: MutableList<CalendarEvent> = mutableListOf(
        CalendarEvent(
            LocalDate(2022, 11, 9),
            LocalTime(11, 30),
            LocalTime(13, 0),
            "Lekarz",
            "Opis wydarzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 9),
            LocalTime(10, 0),
            LocalTime(11, 30),
            "Sambo",
            "Opis"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 9),
            LocalTime(16, 0),
            LocalTime(18, 30),
            "Lekarz",
            "Opis wydarzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 10),
            LocalTime(10, 0),
            LocalTime(11, 30),
            "Lekarz",
            "Opis wydarzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 10),
            LocalTime(13, 0),
            LocalTime(15, 30),
            "Lekasghasfdgasdarz",
            "Opis wydaasdgadsrzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 13),
            LocalTime(10, 0),
            LocalTime(11, 30),
            "Opis wydarzenia",
            ""
        ),
        CalendarEvent(
            LocalDate(2022, 11, 13),
            LocalTime(16, 0),
            LocalTime(18, 30),
            "Lekgsjfgarz",
            "Opis wydasgfjdgfrzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 19),
            LocalTime(16, 0),
            LocalTime(18, 30),
            "Lekgsjfgarz",
            "Opis wydasgfjdgfrzenia"
        ),
        CalendarEvent(
            LocalDate(2022, 11, 19),
            LocalTime(10, 0),
            LocalTime(11, 30),
            "Opis wydarzenia",
            ""
        ),
    )

    //NOTIFICATIONS
    var notificationItems: MutableList<NotificationItem> = mutableListOf(
        NotificationItem(
            name = "IBUPROM",
            interval = "Codziennie",
            timeList = mutableListOf("10:00", "15:00", "20:00")
        ),
        NotificationItem(
            name = "Donepezil",
            interval = "Co 2 dni",
            timeList = mutableListOf("10:00", "20:00")
        )
    )


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

    fun listenToGeofenceStatus() {
        repository.listenToGeofenceStatus(this)
    }

    fun deleteShowAlarm() {
        repository.deleteShowAlarm(this)
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
}