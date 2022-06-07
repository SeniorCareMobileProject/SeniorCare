package com.SeniorCareMobileProject.seniorcare.data

import android.util.Log
import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

object Database {
    private lateinit var database: DatabaseReference
    private val url = "https://senior-care-mobile-proje-a58b2-default-rtdb.europe-west1.firebasedatabase.app/"

    fun initialDatabase(){
        database = FirebaseDatabase.getInstance(url).reference
    }

    fun writeNewUser(userId: String, email: String, firstName: String, lastName: String, function: String) {
        val user = User(email, firstName, lastName, function)
        database.child("users").child(userId).setValue(user)
    }

    fun writeNewConnectedWith(userId: String, connectingID: String){
        database.child("users").child(userId).child("connectedWith").child(connectingID).setValue("")
    }

    fun readTest(){
        database.child("users").child("LhJ5NcLjNWhqbYAhqHpnoaCtXRp2").get().addOnSuccessListener {
            val userData : User? = it.getValue<User>()
            Log.i("test", "Got value ${userData?.firstName}")
        }.addOnFailureListener{
            Log.e("test", "Error getting data", it)
        }
    }
}