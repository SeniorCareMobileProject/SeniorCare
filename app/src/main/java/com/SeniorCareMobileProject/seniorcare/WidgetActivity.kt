package com.SeniorCareMobileProject.seniorcare

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SeniorMainView
import com.SeniorCareMobileProject.seniorcare.ui.views.Senior.SosCascadeView

class WidgetActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    private val requestCall = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = NavigationScreens.SosCascadeView.name){
                composable(NavigationScreens.SosCascadeView.name) {
                    SosCascadeView(navController, sharedViewModel)

                }
                composable(NavigationScreens.SeniorMainScreen.name) {
                    startActivity(Intent(context,MainActivity::class.java))
                }
            }
        }

        sharedViewModel.sosButtonClicked.observe(this, Observer{
                value -> if(value >=0) {
            makePhoneCall(sharedViewModel.phoneNumbers[sharedViewModel.sosButtonClicked.value!!])
            if(sharedViewModel.sosButtonClicked.value!!>=sharedViewModel.phoneNumbers.size-1){
                sharedViewModel.sosButtonClicked.value = -1
            }
        }
        }
        )
    }

    fun makePhoneCall(number: String) {
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this@WidgetActivity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@WidgetActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val phoneNumber = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber)))
            }
        } else {
            Toast.makeText(
                this@WidgetActivity,
                "Nie podano numeru telefonu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}