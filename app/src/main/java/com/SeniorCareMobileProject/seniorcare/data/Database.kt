package com.SeniorCareMobileProject.seniorcare.data

import com.SeniorCareMobileProject.seniorcare.data.dao.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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
}