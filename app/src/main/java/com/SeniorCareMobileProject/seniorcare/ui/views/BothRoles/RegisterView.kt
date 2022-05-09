package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.firebase.FirebaseAuthentication
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.InputBoxPlaceholder
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.ktx.Firebase

@Composable
fun RegisterView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            //.background(Color.Green)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            val email = simpleOutlinedTextFieldSample()
            val password = passwordTextField()

            Button(
                onClick = {
                    //FirebaseAuthentication.displayToast("$email + $password")
                    FirebaseAuthentication.startAuthentication()
                    FirebaseAuthentication.registerUser(email, password)
                          },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Confirm")
            }
        }
        Column(Modifier.weight(128f)) {
            NavButton(navController, "Register", "FirstStartUpScreen")
        }
    }
}

@Composable
fun simpleOutlinedTextFieldSample(): String {
    var text by remember { mutableStateOf("test") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("E-mail") },
        modifier = Modifier.padding(16.dp)
    )
    return text
}

@Composable
fun passwordTextField(): String {
    var password by rememberSaveable { mutableStateOf("") }

    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.padding(16.dp)
    )
    return password
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        RegisterView(navController, sharedViewModel)
    }
}
