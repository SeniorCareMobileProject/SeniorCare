package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.ScrollState
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.data.util.Resource
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.IconTextButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TextFilledButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.SeniorCareMobileProject.seniorcare.R
import com.google.firebase.auth.AuthResult

@Composable
fun LoginView(navController: NavController, sharedViewModel: SharedViewModel) {
    val scrollState = remember { ScrollState(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigate(NavigationScreens.ChooseLoginMethodScreen.name) }
        )

        Column(
            modifier = Modifier
                .padding(top = 46.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Logowanie", color = MaterialTheme.colors.primary, style = h1)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 46.dp),
            verticalArrangement = Arrangement.spacedBy(29.dp)
        ) {
            InputFieldLabelIcon(
                text = "Twój adres email",
                onValueChange = {},
                fieldLabel = "Email",
                iconName = "alternate_email",
                viewModelVariable = sharedViewModel.email
            )
            InputFieldLabelIcon(
                text = "Twoje hasło",
                onValueChange = {},
                fieldLabel = "Hasło",
                iconName = "lock",
                viewModelVariable = sharedViewModel.password,
                isPassword = true
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = "Zapomniałeś hasło?",
                fontSize = 16.sp,
                color = MaterialTheme.colors.primary,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { navController.navigate(NavigationScreens.PasswordRecoveryScreen.name) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            LoginButton(navController, "Zaloguj się", sharedViewModel)
            Divider(color = Color.Black, thickness = 1.dp)
            IconTextButton(
                navController,
                stringResource(R.string.continue_with_Google),
                "google",
                "CarerMainScreen"
            )

            Row(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Nie masz konta?", modifier = Modifier.padding(horizontal = 8.dp))
                Text(
                    text = "Zarejestruj się",
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primary,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { navController.navigate("SignUpEmailScreen") }
                )
            }
        }
    }
}

@Composable
fun LoginButton(navController: NavController, text: String, sharedViewModel: SharedViewModel) {
    Button(
        onClick = {
            sharedViewModel.loginUser(sharedViewModel.email.value, sharedViewModel.password.value)
            navController.navigate("LoadingLoginView")
        },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
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
