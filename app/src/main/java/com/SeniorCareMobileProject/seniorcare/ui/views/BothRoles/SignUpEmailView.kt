package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.ChooseRoleSection
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TextFilledButton


@Composable
fun SignUpEmailView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current

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
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                Text(text = "Rejestracja", color = MaterialTheme.colors.primary, style = h1)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 5.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                InputFieldLabelIcon(
                    text = "Wprowadź swoje na imię",
                    onValueChange = {},
                    fieldLabel = "Imię",
                    iconName = "",
                    viewModelVariable = sharedViewModel.firstName
                )
                InputFieldLabelIcon(
                    text = "Wprowadź swoje nazwisko",
                    onValueChange = {},
                    fieldLabel = "Nazwisko",
                    iconName = "",
                    viewModelVariable = sharedViewModel.lastName
                )
            }

            ChooseRoleSection(sharedViewModel)

            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp)
            ) {
                RegisterButton(navController, "Zarejestruj", sharedViewModel, context)

                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Już masz konto?", modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = "Zaloguj się",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.primary,
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { navController.navigate("LoginScreen") }
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterButton(
    navController: NavController,
    text: String,
    sharedViewModel: SharedViewModel,
    context: Context
) {
    Button(
        onClick = {
            if (sharedViewModel.isFunctionCarer.value) {
                sharedViewModel.function.value = "Carer"
            }
            if (sharedViewModel.isFunctionSenior.value) {
                sharedViewModel.function.value = "Senior"
            }

            if (sharedViewModel.function.value == "") {
                Toast.makeText(context, "You must choose a function", Toast.LENGTH_LONG).show()
            } else {
                sharedViewModel.registerUser(
                    sharedViewModel,
                    sharedViewModel.email.value,
                    sharedViewModel.password.value,
                    sharedViewModel.firstName.value,
                    sharedViewModel.lastName.value,
                    sharedViewModel.function.value
                )
                navController.navigate("LoadingRegisterView")
            }
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
fun SignUpEmailViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SignUpEmailView(navController, sharedViewModel)
    }
}