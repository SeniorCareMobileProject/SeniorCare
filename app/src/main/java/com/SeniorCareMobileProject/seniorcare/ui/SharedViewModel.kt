package com.SeniorCareMobileProject.seniorcare.ui

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.PairingData
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    //location
    val onGeofenceRequest = MutableLiveData<Boolean>(false)
    val seniorLocalization = mutableStateOf(LatLng(52.408839, 16.906782))

    val localizationAccuracy = mutableStateOf(50f)
    val location = mutableStateOf<Location?>(null)


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

    // user data
    val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData

    // for pairing users
    val pairingCode: MutableLiveData<String?> = MutableLiveData("")
    val pairingData: MutableLiveData<PairingData> = MutableLiveData()
    val pairingStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    // senior
    var codeInput = mutableStateOf("")
    val pairingDataStatus = MutableLiveData<Resource<PairingData>>()

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
}