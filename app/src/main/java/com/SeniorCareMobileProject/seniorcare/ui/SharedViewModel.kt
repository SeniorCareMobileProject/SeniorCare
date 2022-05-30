package com.SeniorCareMobileProject.seniorcare.ui

import androidx.lifecycle.*
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.data.dao.User

class SharedViewModel : ViewModel() {

    private val _userData: LiveData<User> = Repository.userData
    val userData: LiveData<User> = Transformations.map(_userData) {
            user -> user
    }

    private val _loggedUserID: LiveData<String> = Repository.loggedUserID
    val loggedUserID: LiveData<String> = Transformations.map(_loggedUserID) {
            string -> string
    }
}