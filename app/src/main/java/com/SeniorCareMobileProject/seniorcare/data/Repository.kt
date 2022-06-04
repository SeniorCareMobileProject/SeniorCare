package com.SeniorCareMobileProject.seniorcare.data

import android.util.Log
import android.widget.Toast
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.data.util.safeCall
import com.SeniorCareMobileProject.seniorcare.firebase.FirebaseAuthentication
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class Repository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val url = "https://senior-care-mobile-proje-a58b2-default-rtdb.europe-west1.firebasedatabase.app/"
    private val database = FirebaseDatabase.getInstance(url)

    private val databaseUserReference = database.getReference("users")

    suspend fun createUser(userEmailAddress: String, userLoginPassword: String, userFirstName: String, userLastName: String, userFunction: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()
                val userId = registrationResult.user?.uid!!
                val newUser = User(userEmailAddress, userFirstName, userLastName, userFunction)
                databaseUserReference.child(userId).setValue(newUser).await()
                Resource.Success(registrationResult)
            }
        }
    }

    suspend fun login(email: String, password: String): Resource<AuthResult> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Resource.Success(result)
            }
        }
    }

    fun loginUser(sharedViewModel: SharedViewModel, email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        Log.d("Login", "Udało się zalogować!")
                        sharedViewModel._userSignUpStatus.postValue(Resource.Success(response))

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        sharedViewModel._userSignUpStatus.postValue(Resource.Error(e.message.toString()))
                        Toast.makeText(MyApplication.context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun readUserDataOnce(sharedViewModel: SharedViewModel){
        val userReference = database.getReference("users/" + firebaseAuth.currentUser!!.uid)
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user != null) {
                    sharedViewModel._userData.value = user
                    sharedViewModel._userDataStatus.postValue(Resource.Success(user))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "loadPost:onCancelled", databaseError.toException())
                sharedViewModel._userDataStatus.postValue(Resource.Error(databaseError.toException().toString()))
            }
        }
        userReference.addValueEventListener(userListener)
        //userReference.addListenerForSingleValueEvent(userListener)
    }
}