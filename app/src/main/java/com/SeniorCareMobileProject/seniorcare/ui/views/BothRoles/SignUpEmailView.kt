package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
//    val context = LocalContext.current

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
                .padding(top = 50.dp)
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
                iconName = "alternate_email"
            )
            InputFieldLabelIcon(
                text = "Twoje hasło",
                onValueChange = {},
                fieldLabel = "Hasło",
                iconName = "lock"
            )
            InputFieldLabelIcon(
                text = "Wprowadź swoje na imię",
                onValueChange = {},
                fieldLabel = "Imię",
                iconName = ""
            )
            InputFieldLabelIcon(
                text = "Wprowadź swoje nazwisko",
                onValueChange = {},
                fieldLabel = "Nazwiko",
                iconName = ""
            )
        }

        ChooseRoleSection()

        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 12.dp)
        ) {
            TextFilledButton(navController, "Zarejestruj", "SignUpEmailVerificationScreen")

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

@Preview(showBackground = true)
@Composable
fun SignUpEmailViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SignUpEmailView(navController, sharedViewModel)
    }
}