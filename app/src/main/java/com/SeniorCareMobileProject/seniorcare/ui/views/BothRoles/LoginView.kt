package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.InputBoxPlaceholder
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.IconTextButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputField
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TextFilledButton
import com.example.seniorcare.R

@Composable
fun LoginView(navController: NavController, sharedViewModel: SharedViewModel) {
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
                iconName = "alternate_email"
            )
            InputFieldLabelIcon(
                text = "Twoje hasło",
                onValueChange = {},
                fieldLabel = "Hasło",
                iconName = "lock"
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
            TextFilledButton(navController, "Zaloguj się", "")
            Divider(color = Color.Black, thickness = 1.dp)
            IconTextButton(
                navController,
                stringResource(R.string.continue_with_Google),
                "google",
                ""
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

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        LoginView(navController, sharedViewModel)
    }
}
