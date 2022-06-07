package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TextFilledButton
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.SignUpVerificationCodeView


@Composable
fun PasswordRecoveryView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigate(NavigationScreens.LoginScreen.name) }
        )

        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Zapomniałeś hasła?", color = MaterialTheme.colors.primary, style = h1)
            Text(text = "Zresetuj hasło przy pomocy maila, na którego zarejestrowałeś konto.")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp),
            verticalArrangement = Arrangement.spacedBy(29.dp)
        ) {
            InputFieldLabelIcon(
                text = "Twój adres email",
                onValueChange = {},
                fieldLabel = "Email",
                iconName = "alternate_email"
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(horizontal = 12.dp)
        ) {
            TextFilledButton(navController, "Zatwierdź", "PasswordRecoveryEmailScreen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordRecoveryViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PasswordRecoveryView(navController, sharedViewModel)
    }
}