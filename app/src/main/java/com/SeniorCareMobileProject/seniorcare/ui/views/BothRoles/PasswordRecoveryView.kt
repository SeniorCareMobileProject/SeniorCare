package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.ScrollState
import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.TextFilledButton
import com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles.SignUpVerificationCodeView
import com.google.firebase.auth.FirebaseAuth


@Composable
fun PasswordRecoveryView(navController: NavController, sharedViewModel: SharedViewModel) {
    val scrollState = remember { ScrollState(0) }

    val context = LocalContext.current
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
                .clickable { navController.navigateUp() }
        )

        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = stringResource(R.string.forgot_password), color = MaterialTheme.colors.primary, style = h1)
            Text(text = stringResource(R.string.reset_password_witn_mail))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp),
            verticalArrangement = Arrangement.spacedBy(29.dp)
        ) {
            InputFieldLabelIcon(
                text = stringResource(R.string.your_email_address),
                onValueChange = {},
                fieldLabel = "Email",
                iconName = "alternate_email",
                viewModelVariable = sharedViewModel.emailForgotPassword
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(horizontal = 12.dp)
        ) {
            ForgotPasswordButton(navController, stringResource(R.string.confirm), "PasswordRecoveryEmailScreen", sharedViewModel, context)
        }
    }
}

@Composable
fun ForgotPasswordButton(navController: NavController, text: String, rout: String, sharedViewModel: SharedViewModel, context: Context) {
    Button(
        onClick = {
            if (Patterns.EMAIL_ADDRESS.matcher(sharedViewModel.emailForgotPassword.value).matches()){
                FirebaseAuth.getInstance().sendPasswordResetEmail(sharedViewModel.emailForgotPassword.value)
                navController.navigate(rout)
            }
            else {
                Toast.makeText(context, context.getString(R.string.type_valid_email), Toast.LENGTH_LONG).show()
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
fun PasswordRecoveryViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        PasswordRecoveryView(navController, sharedViewModel)
    }
}