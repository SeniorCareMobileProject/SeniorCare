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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SmallButtonNoPressedState


@Composable
fun SignUpEmailVerificationView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val iconId = remember("mail") {
        context.resources.getIdentifier(
            "mail",
            "drawable",
            context.packageName
        )
    }
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
                .clickable { navController.navigate(NavigationScreens.SignUpEmailScreen.name) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 138.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "mail",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(192.dp)
                        .height(192.dp)
                )
            }
            Text(
                text = "Wysłaliśmy maila na twoje konto email z linkiem weryfikacyjnym.\n" +
                        "\n" +
                        "Kliknij link, aby zweryfikować swój adres email.",
                modifier = Modifier.padding(horizontal = 42.dp), fontWeight = FontWeight.Medium
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            SmallButtonNoPressedState(text = "Wyślij ponownie")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpEmailVerificationViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        SignUpEmailVerificationView(navController, sharedViewModel)
    }
}