package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.h1
import com.SeniorCareMobileProject.seniorcare.ui.h4
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.*
import com.example.seniorcare.R

@Composable
fun FirstStartUpView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)

    ) {
        Column(Modifier.weight(15f)) {

        }
        Column(Modifier.weight(30f).padding(24.dp)) {
            Text(stringResource(id = R.string.welcome_to), style = h4)
            Text("Mobilnym Opiekunie", style = h1, color = MaterialTheme.colors.primary)

        }
//        Column(Modifier.weight(50f)) {
//            IconTextButton(stringResource(R.string.continue_with_Google), "google")
//            IconTextButton("Kontynuuj z Email", "alternate_email")
//        }
        Column(Modifier
            .weight(50f)
            .fillMaxWidth()
        )
            {
            IconTextButton(stringResource(R.string.continue_with_Google), "google")
            IconTextButton("Kontynuuj z Email", "alternate_email")
        }

    }

//    Button(
//        onClick = { },
//        shape = RoundedCornerShape(20.dp),
//        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        Text(
//            text = "Text",
//            color = Color.White,
//            textAlign = TextAlign.Center,
//            lineHeight = 24.sp,
//            style = TextStyle(
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold))
//    }
}

@Preview(showBackground = true)
@Composable
fun FirstStartUpViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        FirstStartUpView(navController, sharedViewModel)
    }
}

