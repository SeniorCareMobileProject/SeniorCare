package com.SeniorCareMobileProject.seniorcare.ui

import android.util.Log
import androidx.lifecycle.*
import com.SeniorCareMobileProject.seniorcare.data.Database
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {

    private val _userFunction = MutableLiveData<String?>()
    val userFunction : LiveData<String?> = _userFunction

    fun setUserFunction(name: String?) {
        _userFunction.value = name
    }

    private val _userData: LiveData<User> = Repository.userData
    val userData: LiveData<User> = Transformations.map(_userData) {
            user -> user
    }

    private val _loggedUserID: LiveData<String> = Repository.loggedUserID
    val loggedUserID: LiveData<String> = Transformations.map(_loggedUserID) {
            string -> string
    }

    fun getUserData(){
        Database.readUserDataListener(this)
    }

    fun readUserFunction(){
        Database.readFunction(this)
    }
}