package com.SeniorCareMobileProject.seniorcare.data

import android.content.Context
import android.provider.Settings.Secure.getString
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.dao.*
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Repository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val url = "https://senior-care-mobile-proje-a58b2-default-rtdb.europe-west1.firebasedatabase.app/"
    private val database = FirebaseDatabase.getInstance(url)

    private val databaseUserReference = database.getReference("users")

    // REGISTRATION
    fun registerUser(sharedViewModel: SharedViewModel, userEmailAddress: String, userLoginPassword: String, userFirstName: String, userLastName: String, userFunction: String) {
        if (userEmailAddress.isNotEmpty() && userLoginPassword.isNotEmpty() || userFirstName.isNotEmpty() && userLastName.isNotEmpty() || userFunction.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()
                    withContext(Dispatchers.Main) {
                        firebaseAuth.currentUser?.sendEmailVerification()

                        val userId = firebaseAuth.currentUser!!.uid
                        val newUser = User(userEmailAddress, userFirstName, userLastName, userFunction)
                        databaseUserReference.child(userId).setValue(newUser).await()

                        sharedViewModel._userSignUpStatus.postValue(Resource.Success(response))
                        Log.d("Rejestracja", "Udało się zarejestrować!")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        sharedViewModel._userSignUpStatus.postValue(Resource.Error(e.message.toString()))
                        Toast.makeText(MyApplication.context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        else {
            sharedViewModel._userSignUpStatus.postValue(Resource.Error("Empty strings"))
            Toast.makeText(MyApplication.context, "Please enter valid information", Toast.LENGTH_LONG).show()
        }
    }

    fun writeNewUserFromGoogle(user: LiveData<User>){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val newUser = User(user.value?.email, user.value?.firstName, user.value?.lastName, user.value?.function)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    databaseUserReference.child(userId).setValue(newUser).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(MyApplication.context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // LOGGING
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
        else {
            sharedViewModel._userSignUpStatus.postValue(Resource.Error("Empty strings"))
            Toast.makeText(MyApplication.context, "Please enter your email and password", Toast.LENGTH_LONG).show()
        }
    }

    // GET DATA
    fun getUserData(sharedViewModel: SharedViewModel){
        val userReference = database.getReference("users/" + firebaseAuth.currentUser!!.uid)
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user != null) {
                    sharedViewModel._userData.value = user
                    sharedViewModel._userDataStatus.postValue(Resource.Success(user))
                }
                else {
                    sharedViewModel._userDataStatus.postValue(Resource.Error("empty data"))
                    sharedViewModel.isDataEmpty.value = true
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

    fun getListOfSeniors(sharedViewModel: SharedViewModel){
        val reference = databaseUserReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("connectedWith")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<HashMap<String, String>>()
                if (data != null){
                    sharedViewModel.listOfAllSeniors.addAll(data.keys)
                    getCurrentSeniorData(sharedViewModel)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getListOfSeniors:onCancelled", databaseError.toException())
                sharedViewModel._currentSeniorDataStatus.postValue(Resource.Error(databaseError.toException().toString()))
            }
        }
        reference.addListenerForSingleValueEvent(listener)
    }

    fun getCurrentSeniorData(sharedViewModel: SharedViewModel){
        val userReference = database.getReference("users/" + sharedViewModel.listOfAllSeniors[0])
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<SeniorAllDAO>()
                if (user != null) {
                    sharedViewModel.currentSeniorData.value = User(user.email, user.firstName, user.lastName, user.function)
                    if (user.location != null){
                        sharedViewModel.seniorLocalization.value = LatLng(user.location.latitude!!, user.location.longitude!!)
                        sharedViewModel.localizationAccuracy.value = user.location.accuracy!!
                    }
                    if (user.geofence != null){
                        sharedViewModel.geofenceLocation.value = LatLng(user.geofence.latitude!!, user.geofence.longitude!!)
                        sharedViewModel.geofenceRadius.value = user.geofence.radius!!
                    }
                    //getSeniorLocation(sharedViewModel)
                    sharedViewModel._currentSeniorDataStatus.postValue(Resource.Success(sharedViewModel.currentSeniorData.value!!))
                }
                else {
                    sharedViewModel._currentSeniorDataStatus.postValue(Resource.Error("no connected senior"))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getCurrentSeniorData:onCancelled", databaseError.toException())
                sharedViewModel._currentSeniorDataStatus.postValue(Resource.Error(databaseError.toException().toString()))
            }
        }
        userReference.addValueEventListener(userListener)
        //userReference.addListenerForSingleValueEvent(userListener)
    }

    // PAIRING
    fun createPairingCodeAndWriteToFirebase(sharedViewModel: SharedViewModel){
        val pairingCodesReference = database.getReference("pairing").child("codes")
        pairingCodesReference.get().addOnSuccessListener {
            if (it != null){
                val allCodesList = it.getValue<HashMap<String, String>>()
                val allCodesString = allCodesList!!.keys
                val allCodes = allCodesString.map { string -> string.toInt()}
                val uniqueCode = getRandomWithExclusion(10000, 99999, allCodes)

                // WRITE PAIRING CODE TO FIREBASE
                pairingCodesReference
                    .child(uniqueCode.toString())
                    .setValue(FirebaseAuth.getInstance().currentUser?.uid)
                sharedViewModel.pairingCode.value = uniqueCode.toString()

                // WRITE USER DATA FOR PAIRING TO FIREBASE
                val userData = sharedViewModel.userData
                val pairingData = PairingData(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    userData.value!!.firstName,
                    userData.value!!.lastName,
                    userData.value!!.email,
                    false
                )
                val pairingDataReference = database.getReference("pairing").child("data")
                pairingDataReference
                    .child(uniqueCode.toString())
                    .setValue(pairingData)
                sharedViewModel.pairingData.value = pairingData
                // SET STATUS LISTENER
                pairingStatusListener(sharedViewModel)
            }
            else {
                val uniqueCode = Random.nextInt(10000, 99999)
                // WRITE PAIRING CODE TO FIREBASE
                pairingCodesReference
                    .child(uniqueCode.toString())
                    .setValue(FirebaseAuth.getInstance().currentUser?.uid)
                // WRITE USER DATA FOR PAIRING TO FIREBASE
                val userData = sharedViewModel.userData
                val pairingData = PairingData(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    userData.value!!.firstName,
                    userData.value!!.lastName,
                    userData.value!!.email,
                    false
                )
                val pairingDataReference = database.getReference("pairing").child("data")
                pairingDataReference
                    .child(uniqueCode.toString())
                    .setValue(pairingData)
                sharedViewModel.pairingData.value = pairingData
                // SET STATUS LISTENER
                pairingStatusListener(sharedViewModel)
            }
        }.addOnFailureListener{
            Log.e("Pairing Code", "Error getting data", it)
        }

    }

    private fun getRandomWithExclusion(start: Int, end: Int, exclude: List<Int>): Int {
        var random = start + Random.nextInt(end - start + 1 - exclude.size)
        for (ex in exclude) {
            if (random < ex) {
                break
            }
            random++
        }
        return random
    }

    private fun pairingStatusListener(sharedViewModel: SharedViewModel){
        val pairingDataReference = database
            .getReference("pairing")
            .child("data")
            .child(sharedViewModel.pairingCode.value.toString())
            .child("status")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue<Boolean?>()
                if (status != null) {
                    sharedViewModel.pairingStatus.value = status
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "pairingStatusListener:onCancelled", databaseError.toException())
            }
        }
        pairingDataReference.addValueEventListener(listener)
    }

    fun removePairingCode(sharedViewModel: SharedViewModel){
        val dataReference = database
            .getReference("pairing")
            .child("data")
            .child(sharedViewModel.pairingCode.value.toString())
        dataReference.removeValue()

        val codeReference = database
            .getReference("pairing")
            .child("codes")
            .child(sharedViewModel.pairingCode.value.toString())
        codeReference.removeValue()
    }

    fun getPairingData(sharedViewModel: SharedViewModel){
        val pairingDataReference = database.getReference("pairing/data/" + sharedViewModel.codeInput.value)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue<PairingData>()
                    if (data != null){
                        sharedViewModel.pairingData.value = data
                        sharedViewModel.pairingDataStatus.postValue(Resource.Success(data))
                    }
                }
                else {
                    sharedViewModel.pairingDataStatus.postValue(Resource.Error("Nie ma takiego kodu"))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getPairingData:onCancelled", databaseError.toException())
                sharedViewModel.pairingDataStatus.postValue(Resource.Error(databaseError.toException().toString()))
            }
        }
        pairingDataReference.addListenerForSingleValueEvent(listener)
    }

    fun getSeniorIDForPairing(sharedViewModel: SharedViewModel){
        val pairingDataReference = database.getReference("pairing")
            .child("data")
            .child(sharedViewModel.pairingCode.value!!)
            .child("seniorID")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<String>()
                if (data != null){
                    sharedViewModel.pairingSeniorID.value = data
                    if (sharedViewModel.pairingSeniorID.value != firebaseAuth.currentUser!!.uid){
                        sharedViewModel.writeNewConnectionStatus.postValue(Resource.Success(data))
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSeniorIDForPairing:onCancelled", databaseError.toException())
            }
        }
        pairingDataReference.addListenerForSingleValueEvent(listener)
    }

    fun writeSeniorIDForPairing(sharedViewModel: SharedViewModel){
        database.getReference("pairing")
            .child("data")
            .child(sharedViewModel.codeInput.value)
            .child("seniorID")
            .setValue(FirebaseAuth.getInstance().currentUser?.uid)
    }

    fun writeNewConnectedWith(userId: String, connectingID: String){
        database.getReference("users").child(userId).child("connectedWith").child(connectingID).setValue("")
    }

    fun updatePairingStatus(sharedViewModel: SharedViewModel){
        database.getReference("pairing")
            .child("data")
            .child(sharedViewModel.codeInput.value)
            .child("status")
            .setValue(true)
    }

    // LOCATION
    fun saveLocationToFirebase(location: LocationDAO?){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseReference = databaseUserReference.child(userId).child("location")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    databaseReference.setValue(location).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LocationFirebase", e.message.toString())
                }
            }
        }
    }

    fun getSeniorLocation(sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllSeniors[0])
            .child("location")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue<LocationDAO>()
                if (location != null) {
                    sharedViewModel.seniorLocalization.value = LatLng(location.latitude!!, location.longitude!!)
                    sharedViewModel.localizationAccuracy.value = location.accuracy!!
                    Log.d("Wspolrzedne", "${sharedViewModel.seniorLocalization.value.latitude} ${sharedViewModel.seniorLocalization.value.longitude}")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSeniorLocation:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(userListener)
    }

    // GEOFENCE
    fun saveGeofenceStatusToFirebase(){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseReference = databaseUserReference.child(userId).child("geofence").child("showAlarm")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    databaseReference.setValue("true").await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveGeofenceStatusToFirebase", e.message.toString())
                }
            }
        }
    }

    fun listenToGeofenceStatus(sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllSeniors[0])
            .child("geofence")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue<GeofenceDAO>()
                if (status != null) {
                    sharedViewModel.onNotficationShow.value = status.showAlarm
                    Log.d("showAalarm", "zmiana na true")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSeniorLocation:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(listener)
    }

    fun saveGeofenceToFirebase(geoFenceLocation: GeofenceDAO, sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllSeniors[0])
            .child("geofence")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(geoFenceLocation).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LocationFirebase", e.message.toString())
                }
            }
        }
    }

    fun getGeofenceForSenior(sharedViewModel: SharedViewModel){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("geofence")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val geofence = snapshot.getValue<GeofenceDAO>()
                if (geofence != null) {
                    sharedViewModel.geofenceLocation.value = LatLng(geofence.latitude!!, geofence.longitude!!)
                    sharedViewModel.geofenceRadius.value = geofence.radius!!
                    Log.d("Wspolrzedne geofence", "${sharedViewModel.geofenceLocation.value.latitude} ${sharedViewModel.geofenceLocation.value.longitude}")

                    sharedViewModel.hasSeniorData.value = true
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSeniorLocation:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(userListener)
    }

    fun deleteShowAlarm(sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllSeniors[0])
            .child("geofence")
            .child("showAlarm")
        reference.removeValue()
    }
}