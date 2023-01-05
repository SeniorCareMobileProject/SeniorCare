package com.wakeupgetapp.seniorcare.ui.views.BothRoles

import androidx.compose.foundation.ScrollState
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.google.firebase.auth.FirebaseAuth


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
                .clickable { navController.navigateUp() }
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
                text = stringResource(id = R.string.send_mail_with_verification),
                modifier = Modifier.padding(horizontal = 42.dp), fontWeight = FontWeight.Medium
            )
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
            SendVerificationEmail(text = stringResource(id = R.string.send_again))
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            EmailVerifiedButton(text = stringResource(id = R.string.verificate_mail_button), navController)
        }
    }
}

@Composable
fun SendVerificationEmail(text: String) {
    val context = LocalContext.current
    Button(
        onClick = {
            Toast.makeText(context, context.getString(R.string.sending_email_again), Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                  },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .width(width = 187.dp)
            .height(height = 30.dp)
            .padding(horizontal = 19.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun EmailVerifiedButton(text: String, navController: NavController) {
    val context = LocalContext.current
    Button(
        onClick = {
            FirebaseAuth.getInstance().currentUser?.reload()
            val user = FirebaseAuth.getInstance().currentUser
            if (user?.isEmailVerified!!){
                navController.navigate("LoadingDataView")
            }
            else {
                Toast.makeText(context, context.getString(R.string.you_need_to_verify), Toast.LENGTH_LONG).show()
            }
                  },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .width(width = 187.dp)
            .height(height = 30.dp)
            .padding(horizontal = 19.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
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