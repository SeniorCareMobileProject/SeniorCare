package com.SeniorCareMobileProject.seniorcare.data

import androidx.lifecycle.MutableLiveData
import com.SeniorCareMobileProject.seniorcare.data.dao.User

object Repository {
    val loggedUserID : MutableLiveData<String> = MutableLiveData()
    val userData : MutableLiveData<User> = MutableLiveData()
}