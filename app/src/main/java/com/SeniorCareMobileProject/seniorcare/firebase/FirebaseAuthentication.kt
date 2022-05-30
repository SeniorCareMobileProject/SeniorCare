package com.SeniorCareMobileProject.seniorcare.firebase

import android.util.Log
import android.widget.Toast
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.data.Database.initialDatabase
import com.SeniorCareMobileProject.seniorcare.data.Database.writeNewUser
import com.SeniorCareMobileProject.seniorcare.data.Repository
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

object FirebaseAuthentication {

    lateinit var auth: FirebaseAuth

    fun startAuthentication(){
        auth = FirebaseAuth.getInstance()
    }

    private fun checkIfAuthInitialize(){
        if (!::auth.isInitialized){
            startAuthentication()
        }
    }

    fun registerUser(email: String, password: String, firstName: String, lastName: String, function: String){
        checkIfAuthInitialize()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        auth.currentUser?.sendEmailVerification()

                        //WRITE TO DATABASE
                        writeNewUser(auth.currentUser!!.uid, email, firstName, lastName, function)
                        Log.d("Rejestracja", "Udało się zarejestrować!")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(MyApplication.context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String){
        checkIfAuthInitialize()
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        Log.d("Login", "Udało się zalogować!")
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(MyApplication.context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun checkLoggedInState() {
        checkIfAuthInitialize()
        if (auth.currentUser == null){
            Repository.loggedUserID.value = "You are not logged in!"
        }
        else {
            Repository.loggedUserID.value = auth.currentUser!!.uid
        }
    }

    fun signOut(){
        checkIfAuthInitialize()
        auth.signOut()
    }
}