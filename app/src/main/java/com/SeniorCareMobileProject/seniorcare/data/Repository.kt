package com.SeniorCareMobileProject.seniorcare.data

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.SeniorCareMobileProject.seniorcare.MyApplication
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
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
                        val newUser = User(
                            email = userEmailAddress,
                            firstName = userFirstName,
                            lastName = userLastName,
                            function = userFunction,
                            battery = 100.0f
                        )
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
        val newUser = User(
            email = user.value?.email,
            firstName = user.value?.firstName,
            lastName = user.value?.lastName,
            function = user.value?.function,
            battery = 100.0f
        )
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
                    user.function?.let { sharedViewModel.saveUserFunctionToLocalRepo(it) }
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
        sharedViewModel.listOfAllConnectedUsersID.clear()
        val reference = databaseUserReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("connectedWith")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<HashMap<String, String>>()
                if (data != null){
                    sharedViewModel.listOfAllConnectedUsersID.addAll(data.keys)
                    sharedViewModel.hasListOfConnectedUsers.value = true
                    getCurrentSeniorData(sharedViewModel)
                    getListOfConnectedUsersNames(sharedViewModel)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getListOfSeniors:onCancelled", databaseError.toException())
                sharedViewModel._currentSeniorDataStatus.postValue(Resource.Error(databaseError.toException().toString()))
            }
        }
        reference.addListenerForSingleValueEvent(listener)
    }

    fun getListOfCarers(sharedViewModel: SharedViewModel){
        sharedViewModel.listOfAllConnectedUsersID.clear()
        val reference = databaseUserReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("connectedWith")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<HashMap<String, String>>()
                if (data != null){
                    sharedViewModel.listOfAllConnectedUsersID.addAll(data.keys)
                    sharedViewModel.hasListOfConnectedUsers.value = true
                    getListOfConnectedUsersNames(sharedViewModel)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getListOfCarers:onCancelled", databaseError.toException())
            }
        }
        reference.addListenerForSingleValueEvent(listener)
    }

    fun getListOfConnectedUsersNames(sharedViewModel: SharedViewModel) {
        sharedViewModel.listOfConnectedUsers.clear()
        for (userId in sharedViewModel.listOfAllConnectedUsersID){
            val reference = databaseUserReference.child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<User>()
                    if (data != null){
                        sharedViewModel.listOfConnectedUsers.add(
                            data.firstName.toString() + " " + data.lastName.toString()
                        )
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("Database", "getListOfConnectedUsersNames:onCancelled", databaseError.toException())
                }
            }
            reference.addListenerForSingleValueEvent(listener)
        }
    }

    fun getCurrentSeniorData(sharedViewModel: SharedViewModel){
        val userReference = database.getReference("users/" + sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<SeniorAllDAO>()
                if (user != null) {
                    sharedViewModel.lastUpdateTime = LocalDateTime.now().toLocalTime().format(
                        DateTimeFormatter.ofPattern("HH:mm"))
                    sharedViewModel.currentSeniorData.value = User(user.email, user.firstName, user.lastName, user.function)
                    if (user.location != null){
                        sharedViewModel.seniorLocalization.value = LatLng(user.location.latitude!!, user.location.longitude!!)
                        sharedViewModel.localizationAccuracy.value = user.location.accuracy!!
                    }
                    if (user.geofence != null){
                        sharedViewModel.geofenceLocation.value = LatLng(user.geofence.latitude!!, user.geofence.longitude!!)
                        sharedViewModel.geofenceRadius.value = user.geofence.radius!!
                    }
                    if (user.medicalInformation != null){
                        sharedViewModel.medInfo.value = MedInfoDAO(
                            user.medicalInformation.firstName,
                            user.medicalInformation.lastName,
                            user.medicalInformation.birthday,
                            user.medicalInformation.illnesses,
                            user.medicalInformation.bloodType,
                            user.medicalInformation.allergies,
                            user.medicalInformation.medication,
                            user.medicalInformation.height,
                            user.medicalInformation.weight,
                            user.medicalInformation.languages,
                            user.medicalInformation.others
                        )
                    }
                    if (user.sos != null) {
                        val newSosNumbers = mutableListOf<String>()
                        val newSosNames = mutableListOf<String>()
                        val newNumberState = mutableListOf<MutableState<String>>()
                        val newNameState = mutableListOf<MutableState<String>>()
                        for (sosNumber in user.sos) {
                            newSosNumbers.add(sosNumber.number)
                            newSosNames.add(sosNumber.name)
                            newNumberState.add(
                                mutableStateOf(sosNumber.number)
                            )
                            newNameState.add(
                                mutableStateOf(sosNumber.name)
                            )
                        }
                        sharedViewModel.sosCascadePhoneNumbers = newSosNumbers
                        sharedViewModel.sosPhoneNumbersNames = newSosNames
                        sharedViewModel.sosSettingsNumberStates = newNumberState
                        sharedViewModel.sosSettingsNameStates = newNameState
                    }
                    if (user.calendar != null) {
                        sharedViewModel.calendarEventsFirebase.clear()
                        for (calendarEventFirebase in user.calendar) {
                            sharedViewModel.calendarEventsFirebase.add(calendarEventFirebase)
                        }
                        sharedViewModel.parseCalendarEventsFirebaseToCalendarEvents()
                    }
                    if (user.notifications != null) {
                        sharedViewModel.notificationItems.clear()
                        for (item in user.notifications){
                            sharedViewModel.notificationItems.add(item)
                        }
                        sharedViewModel.notificationitemsLiveData.value = sharedViewModel.notificationItems
                    }
                    if (user.battery != null) {
                        sharedViewModel.batteryPct.value = user.battery
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
        val pairingCodesReference = database.reference.child("pairing").child("codes")
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
                val pairingDataReference = database.reference.child("pairing").child("data")
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
                val pairingDataReference = database.reference.child("pairing").child("data")
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
        if (sharedViewModel.pairingCode.value != "") {
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
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
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
    fun saveGeofenceStatusToFirebase(geofenceStatus: String){
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseReference = databaseUserReference.child(userId).child("geofence").child("showAlarm")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    databaseReference.setValue(geofenceStatus).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveGeofenceStatusToFirebase", e.message.toString())
                }
            }
        }
    }

    fun saveGeofenceToFirebase(geoFenceLocation: GeofenceDAO, sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
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

//    fun fetchGeofencesForSenior(): Flow<List<GeofenceDAO>?> = callbackFlow {
//
//        val listener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val data = dataSnapshot.getValue<List<GeofenceDAO>>()
//                trySend(data).isSuccess
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//        }
//
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        val reference = database.getReference("users")
//            .child(userId)
//            .child("geofence")
//        reference.addListenerForSingleValueEvent(listener)
//
//        awaitClose{
//            //remove listener here
//            reference.removeEventListener(listener)
//        }
//    }

    fun fetchGeofencesForSenior(): Flow<GeofenceDAO> = callbackFlow {

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<GeofenceDAO>()
                try {
                    if (data != null)
                    {trySend(data) }
                }
                finally {

                }
        }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("geofence")
        reference.addValueEventListener(listener)

        awaitClose{
            //remove listener here
            reference.removeEventListener(listener)
        }
    }

    fun saveMedicalInfo(sharedViewModel: SharedViewModel){
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
            .child("medicalInformation")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(sharedViewModel.medInfo.value).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MedInfoFirebase", e.message.toString())
                }
            }
        }
    }

    fun getMedicalInformationForSenior(sharedViewModel: SharedViewModel) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("medicalInformation")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medInfo = snapshot.getValue<MedInfoDAO>()
                if (medInfo != null) {
                    sharedViewModel.medInfo.value = MedInfoDAO(
                        medInfo.firstName,
                        medInfo.lastName,
                        medInfo.birthday,
                        medInfo.illnesses,
                        medInfo.bloodType,
                        medInfo.allergies,
                        medInfo.medication,
                        medInfo.height,
                        medInfo.weight,
                        medInfo.languages,
                        medInfo.others
                    )
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSeniorMedInfo:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(userListener)
    }

    fun getSosNumbersForSenior(sharedViewModel: SharedViewModel) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("sos")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sosNumbers = snapshot.getValue<ArrayList<SosNumberDAO>>()
                if (sosNumbers != null) {
                    val newSosNumbers = mutableListOf<String>()
                    val newSosNames = mutableListOf<String>()
                    val newNumberState = mutableListOf<MutableState<String>>()
                    val newNameState = mutableListOf<MutableState<String>>()
                    for (sosNumber in sosNumbers) {
                        newSosNumbers.add(sosNumber.number)
                        newSosNames.add(sosNumber.name)
                        newNumberState.add(
                            mutableStateOf(sosNumber.number))
                        newNameState.add(
                            mutableStateOf(sosNumber.name))
                    }
                    sharedViewModel.sosCascadePhoneNumbers = newSosNumbers
                    sharedViewModel.sosPhoneNumbersNames = newSosNames
                    sharedViewModel.sosSettingsNumberStates = newNumberState
                    sharedViewModel.sosSettingsNameStates = newNameState
                    sharedViewModel.saveSosNumbersToLocalRepo()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "getSosNumbersForSenior:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(userListener)
    }

    fun saveSosNumbersToFirebase(sharedViewModel: SharedViewModel) {
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
            .child("sos")
        val allNumbers: ArrayList<SosNumberDAO> = arrayListOf()
        for (i in 0 until sharedViewModel.sosCascadePhoneNumbers.size) {
            val sosNumber = SosNumberDAO(
                name = sharedViewModel.sosPhoneNumbersNames[i],
                number = sharedViewModel.sosCascadePhoneNumbers[i]
            )
            allNumbers.add(sosNumber)
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(allNumbers).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveSosNumbersToFirebase:", e.message.toString())
                }
            }
        }
    }

    fun saveCalendarEvents(sharedViewModel: SharedViewModel) {
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
            .child("calendar")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(sharedViewModel.calendarEventsFirebase).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveCalendarEventsToFirebase:", e.message.toString())
                }
            }
        }
    }

    fun loadCalendarEventsForSenior(sharedViewModel: SharedViewModel) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("calendar")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val calendarEventsFirebase = snapshot.getValue<ArrayList<CalendarEventDAO>>()
                if (calendarEventsFirebase != null) {
                    sharedViewModel.calendarEventsFirebase.clear()
                    for (calendarEventFirebase in calendarEventsFirebase) {
                        sharedViewModel.calendarEventsFirebase.add(calendarEventFirebase)
                    }
                    sharedViewModel.parseCalendarEventsFirebaseToCalendarEvents()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "loadCalendarEventsForSenior:onCancelled", databaseError.toException())
            }
        }
        reference.addValueEventListener(userListener)
    }

    fun saveNotificationsToFirebase(sharedViewModel: SharedViewModel) {
        val reference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
            .child("notifications")

        val dataToSend: ArrayList<NotificationItem> = ArrayList()
        for (item in sharedViewModel.notificationItems){
            dataToSend.add(item)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(dataToSend).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveNotificationsToFirebase", e.message.toString())
                }
            }
        }
    }

    fun getNotificationsForSenior(): Flow<ArrayList<NotificationItem>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<ArrayList<NotificationItem>>()
                try {
                    if (data != null)
                    { trySend(data) }
                }
                finally {
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("notifications")
        reference.addValueEventListener(listener)

        awaitClose{
            //remove listener here
            reference.removeEventListener(listener)
        }
    }

    fun saveBatteryInfoToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("battery")

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            MyApplication.context?.registerReceiver(null, ifilter)
        }
        val dataToSend = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(dataToSend).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveBatteryInfoToFirebase", e.message.toString())
                }
            }
        }
    }

    fun getBatteryInfoFromAllSeniors(): Flow<HashMap<String, Float>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, String>>()
                try {
                    if (data != null) {
                        val dataToSend = ArrayList<String>()
                        dataToSend.addAll(data.keys)
                        CoroutineScope(Dispatchers.Main).launch {
                            getBatteryInfoFromAllSeniors(dataToSend).collectLatest {
                                trySend(it)
                            }
                        }
                    }
                }
                finally {
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("connectedWith")
        reference.addValueEventListener(listener)

        awaitClose{
            //remove listener here
            reference.removeEventListener(listener)
        }
    }

    private fun getBatteryInfoFromAllSeniors(listOfId: ArrayList<String>): Flow<HashMap<String, Float>> = callbackFlow {
        val batteryInfoWithName = HashMap<String, Float>()
        for (userId in listOfId) {
            val reference = databaseUserReference.child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<User>()
                    if (data != null){
                        batteryInfoWithName[data.firstName.toString() + " " + data.lastName.toString()] =
                            data.battery!!
                    }
                    if (batteryInfoWithName.size == listOfId.size){
                        trySend(batteryInfoWithName)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("Database", "getBatteryInfoFromAllSeniors:onCancelled", databaseError.toException())
                }
            }
            reference.addListenerForSingleValueEvent(listener)
        }
        awaitClose{
            //remove listener here
            //reference.removeEventListener(listener)
        }
    }

    fun saveTrackingSettingsSenior(seniorInSafeZone: Boolean?, isSeniorAware: Boolean?) {
        val path = FirebaseAuth.getInstance().currentUser!!.uid

        val reference = database.getReference("users")
            .child(path)
            .child("trackingSettings")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    if (seniorInSafeZone != null)
                        reference.child("seniorInSafeZone").setValue(seniorInSafeZone).await()
                    if (isSeniorAware != null)
                        reference.child("isSeniorAware").setValue(isSeniorAware).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveTrackingSettingsSenior", e.message.toString())
                }
            }
        }
    }

    fun getTrackingSettingsSenior(): Flow<SeniorTrackingSettingsDao> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<SeniorTrackingSettingsDao>()
                try {
                    if (data != null)
                    { trySend(data) }
                }
                finally {
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        val path = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(path)
            .child("trackingSettings")
        reference.addValueEventListener(listener)

        awaitClose{
            //remove listener here
            reference.removeEventListener(listener)
        }
    }

    fun saveTrackingSettingsCarer(dataToSend: Boolean, currentSeniorId: String) {
        val path = currentSeniorId

        val reference = database.getReference("users")
            .child(path)
            .child("trackingSettings")
            .child("carerOpenApp")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    reference.setValue(dataToSend).await()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("saveTrackingSettingsCarer", e.message.toString())
                }
            }
        }
    }

    fun getTrackingSettingsAllSeniorsForCarer(): Flow<HashMap<String, SeniorTrackingSettingsDao>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, String>>()
                try {
                    if (data != null) {
                        val dataToSend = ArrayList<String>()
                        dataToSend.addAll(data.keys)
                        CoroutineScope(Dispatchers.Main).launch {
                            getTrackingSettingsAllSeniorsForCarer(dataToSend).collectLatest {
                                trySend(it)
                            }
                        }
                    }
                }
                finally {
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val reference = database.getReference("users")
            .child(userId)
            .child("connectedWith")
        reference.addValueEventListener(listener)

        awaitClose{
            //remove listener here
            reference.removeEventListener(listener)
        }
    }

    private fun getTrackingSettingsAllSeniorsForCarer(listOfId: ArrayList<String>): Flow<HashMap<String, SeniorTrackingSettingsDao>> = callbackFlow {
        val trackingSettingAll = HashMap<String, SeniorTrackingSettingsDao>()
        for (userId in listOfId) {
            val reference = databaseUserReference.child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<SeniorAllDAO>()
                    if (data != null){
                        trackingSettingAll[data.firstName.toString() + " " + data.lastName.toString()] =
                            data.trackingSettings!!
                    }
                    if (trackingSettingAll.size == listOfId.size){
                        trySend(trackingSettingAll)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("Database", "getTrackingSettingsAllSeniorsForCarer:onCancelled", databaseError.toException())
                }
            }
            reference.addListenerForSingleValueEvent(listener)
        }
        awaitClose{
            //remove listener here
            //reference.removeEventListener(listener)
        }
    }

    fun disconnectUsers(sharedViewModel: SharedViewModel) {
        val seniorReference = database.getReference("users")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
            .child("connectedWith")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        seniorReference.removeValue()
        val carerReference = database.getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("connectedWith")
            .child(sharedViewModel.listOfAllConnectedUsersID[sharedViewModel.currentSeniorIndex])
        carerReference.removeValue()
    }
}