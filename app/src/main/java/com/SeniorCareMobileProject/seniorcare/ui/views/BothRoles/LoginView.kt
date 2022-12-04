package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.util.LoadingState
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.body_16
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.InputFieldLabelIcon
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginView(navController: NavController, sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val scrollState = remember { ScrollState(0) }

    // GOOGLE
    val state by sharedViewModel.loadingGoogleLogInState.collectAsState()
    // Equivalent of onActivityResult
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                sharedViewModel.loginWithCredential(credential)
                sharedViewModel.userDataFromGoogle(account.email!!, account.displayName!!)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                .padding(horizontal = 24.dp)
                .fillMaxHeight()
        ) {
            Text(text = "Logowanie", color = MaterialTheme.colors.primary, style = h1)
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
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
                GoogleLoginInButton(
                    navController,
                    stringResource(R.string.continue_with_Google),
                    "google",
                    "",
                    sharedViewModel,
                    launcher
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

    when (state.status) {
        LoadingState.Status.SUCCESS -> {
            LaunchedEffect("") {
                sharedViewModel.getUserData()
                navController.navigate("LoadingDataView")
            }
        }
        LoadingState.Status.FAILED -> {
            Toast.makeText(context, "Error with signing in", Toast.LENGTH_LONG).show()
        }
        else -> {}
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

@Composable
fun GoogleLoginInButton(
    navController: NavController,
    text: String,
    iconName: String,
    rout: String,
    sharedViewModel: SharedViewModel,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    var iconId = -1
    if (!iconName.equals("")) {
        val context = LocalContext.current
        iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }
    }

    val context = LocalContext.current
    val token = stringResource(R.string.webclient_id)

    Button(
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            launcher.launch(googleSignInClient.signInIntent)
        },
        border = BorderStroke(1.5.dp, Color.Black),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (!iconId.equals(-1)) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Unspecified
            )
        }
        Spacer(
            modifier = Modifier
                .width(width = 8.dp)
        )
        Text(
            text = text,
            color = Color(0xff070707),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            style = body_16
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
