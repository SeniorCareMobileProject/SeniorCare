package com.SeniorCareMobileProject.seniorcare.ui

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.LocationDAO
import com.SeniorCareMobileProject.seniorcare.data.dao.PairingData
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.LoadingState
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent

class SharedViewModel : ViewModel(), KoinComponent {

    //location
    val onGeofenceRequest = MutableLiveData<Boolean>(false)
    val seniorLocalization = mutableStateOf(LatLng(52.408839, 16.906782))
    val localizationAccuracy = mutableStateOf(50f)
    val location = mutableStateOf<Location?>(null)
    val seniorLocalizationZoom = mutableStateOf(17f)
    val mapFullscreen = mutableStateOf(false)
    val resetCamera = mutableStateOf(false)
    val locationBeforeFreeingCam = mutableStateOf(LatLng(52.408839, 16.906782))

    val onNotficationShow = MutableLiveData<Boolean>(false)

    //geofence
    val geofenceLocation = mutableStateOf(LatLng(1.0, 1.0))
    val geofenceRadius = mutableStateOf(1)
    val newGeofenceLocation = mutableStateOf(LatLng(1.0, 1.0))
    val newGeofenceRadius = mutableStateOf(1)


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
    var isAfterRegistration : Boolean = false

    // for checking request state
    val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus
    val _userDataStatus = MutableLiveData<Resource<User>>()
    val userDataStatus: LiveData<Resource<User>> = _userDataStatus
    val _currentSeniorDataStatus = MutableLiveData<Resource<User>>()
    val currentSeniorDataStatus: LiveData<Resource<User>> = _currentSeniorDataStatus


    // user data
    val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData
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
    val sosButtonClicked: MutableLiveData<Boolean> = MutableLiveData(false)

    private val repository = Repository()

    fun loginUser(userEmailAddress: String, userLoginPassword: String) {
        _userSignUpStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            repository.loginUser(this@SharedViewModel, userEmailAddress, userLoginPassword)
        }
    }

    fun registerUser(sharedViewModel: SharedViewModel, userEmailAddress: String, userLoginPassword: String, userFirstName: String, userLastName: String, userFunction: String){
        _userSignUpStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            repository.registerUser(this@SharedViewModel, userEmailAddress, userLoginPassword, userFirstName, userLastName, userFunction)
        }
    }

    fun getUserData(){
        viewModelScope.launch(Dispatchers.Main) {
            _userDataStatus.postValue(Resource.Loading())
            repository.getUserData(this@SharedViewModel)
        }
    }

    fun getCurrentSeniorData(){
        viewModelScope.launch(Dispatchers.Main) {
            _currentSeniorDataStatus.postValue(Resource.Loading())
            repository.getListOfSeniors(this@SharedViewModel)
        }
    }

    fun getSeniorIDForPairing(){
        repository.getSeniorIDForPairing(this)
    }

    fun createPairingCode(){
        repository.createPairingCodeAndWriteToFirebase(this)
    }

    fun deletePairingCode(){
        repository.removePairingCode(this)
    }

    fun getPairingData(){
        viewModelScope.launch(Dispatchers.Main) {
            pairingDataStatus.postValue(Resource.Loading())
            repository.getPairingData(this@SharedViewModel)
        }
    }

    fun writeSeniorIDForPairing(){
        repository.writeSeniorIDForPairing(this)
    }

    fun writeNewConnectionWith(carerID: String){
        repository.writeNewConnectedWith(FirebaseAuth.getInstance().currentUser!!.uid, carerID)
    }

    fun updatePairingStatus(){
        repository.updatePairingStatus(this)
    }

    // GOOGLE SIGN IN
    fun userDataFromGoogle(email: String, displayName: String){
        val fullName = displayName.split(" ")
        val firstName = fullName[0]
        val lastName = fullName[1]
        _userData.value = User(email, firstName, lastName, this.function.value)
    }

    fun writeNewUserFromGoogle(userData: LiveData<User>){
        repository.writeNewUserFromGoogle(userData)
    }

    val loadingGoogleSignInState = MutableStateFlow(LoadingState.IDLE)

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            loadingGoogleSignInState.emit(LoadingState.LOADING)
            Firebase.auth.signInWithCredential(credential).await()
            loadingGoogleSignInState.emit(LoadingState.LOADED)
        } catch (e: Exception) {
            loadingGoogleSignInState.emit(LoadingState.error(e.localizedMessage))
        }
    }

    // LOCATION

    fun saveLocationToFirebase(){
        val locationAll = this@SharedViewModel.location.value
        val location = LocationDAO(locationAll?.latitude, locationAll?.longitude, locationAll?.accuracy)
        repository.saveLocationToFirebase(location)
    }
}