package com.SeniorCareMobileProject.seniorcare.firebase

import android.util.Log
import android.widget.Toast
import com.SeniorCareMobileProject.seniorcare.MyApplication
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

    fun registerUser(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        auth.currentUser?.sendEmailVerification()
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

    fun displayToast(text : String) {
        Toast.makeText(MyApplication.context, text, Toast.LENGTH_LONG).show()
    }
}