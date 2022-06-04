package com.SeniorCareMobileProject.seniorcare.ui

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    // for getting input
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var isFunctionCarer = mutableStateOf(false)
    var isFunctionSenior = mutableStateOf(false)
    var function = mutableStateOf("")

    // for checking request state
    val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    val _userDataStatus = MutableLiveData<Resource<User>>()
    val userDataStatus: LiveData<Resource<User>> = _userDataStatus

    // user data
    val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData

    private val repository = Repository()

    fun createUser(userEmailAddress: String, userLoginPassword: String, userFirstName: String, userLastName: String, userFunction: String) {
        val error =
            if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty() || userFirstName.isEmpty() || userLastName.isEmpty() || userFunction.isEmpty()) {
                "Empty Strings"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailAddress).matches()) {
                "Not a valid Email"
            } else null

        error?.let {
            _userSignUpStatus.postValue(Resource.Error(it))
            return
        }
        _userSignUpStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = repository.createUser(userEmailAddress, userLoginPassword, userFirstName, userLastName, userFunction)
            _userSignUpStatus.postValue(registerResult)
        }
    }

    fun signInUser(userEmailAddress: String, userLoginPassword: String) {
        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()){
            _userSignUpStatus.postValue(Resource.Error("Empty Strings"))
        } else {
            _userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = repository.login(userEmailAddress, userLoginPassword)
                _userSignUpStatus.postValue(loginResult)
            }
        }
    }

    fun getUserDataNew(){
        viewModelScope.launch(Dispatchers.Main) {
            _userDataStatus.postValue(Resource.Loading())
            repository.readUserDataOnce(this@SharedViewModel)
        }
    }
}