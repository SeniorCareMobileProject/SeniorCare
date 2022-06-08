package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.h4
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.SeniorCareMobileProject.seniorcare.R

@Composable
fun ChooseLoginMethodView(navController: NavController, sharedViewModel: SharedViewModel) {
    val scrollState = remember { ScrollState(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .fillMaxHeight()
            .verticalScroll(scrollState)

    ) {
        Column(
            Modifier
                .padding(top = 160.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(stringResource(id = R.string.welcome_to), style = h4)
            Text("Mobilnym Opiekunie", style = h1, color = MaterialTheme.colors.primary)

        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 68.dp)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
        {
            Text(text = "Zarejestruj nowe konto:", modifier = Modifier.padding(bottom = 6.dp))
            IconTextButton(
                navController,
                stringResource(R.string.continue_with_Google),
                "google",
                "SignUpGoogleScreen"
            )
            IconTextButton(
                navController,
                "Kontynuuj z Email",
                "alternate_email",
                "SignUpEmailScreen"
            )
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
                        .clickable { navController.navigate("LoginScreen") },
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ChooseLoginMethodViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        ChooseLoginMethodView(navController, sharedViewModel)
    }
}

