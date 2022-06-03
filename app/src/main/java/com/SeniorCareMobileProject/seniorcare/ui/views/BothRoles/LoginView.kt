package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.Database
import com.SeniorCareMobileProject.seniorcare.firebase.FirebaseAuthentication
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.ktx.Firebase

@Composable
fun LoginView(navController: NavController, sharedViewModel: SharedViewModel) {
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
            val email = simpleOutlinedTextFieldSample("E-mail")
            val password = passwordTextField()


            Button(
                onClick = {
                    FirebaseAuthentication.loginUser(email, password)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    FirebaseAuthentication.signOut()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Sign out")
            }
            TextLive(sharedViewModel)

            Button(
                onClick = {
                    Database.readTest()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Read test")
            }

            Button(
                onClick = {
                    Database.writeNewConnectedWith("LhJ5NcLjNWhqbYAhqHpnoaCtXRp2", "Co44st0qc8U6jsqvSMLuMQNfVPg2")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Connected write test")
            }
        }

        Column(Modifier.weight(128f)) {
            NavButton(navController, "Login", "LoadingUserDataView")
        }

    }
}

@Composable
fun TextLive(sharedViewModel: SharedViewModel) {
    FirebaseAuthentication.checkLoggedInState()
    val loggedUser by sharedViewModel.loggedUserID.observeAsState()
    if (loggedUser != null){
        Text(text = loggedUser!!)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}
