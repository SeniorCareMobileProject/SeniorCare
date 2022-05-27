package com.SeniorCareMobileProject.seniorcare.ui.views.BothRoles

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.CommonTemplateButton
import com.SeniorCareMobileProject.seniorcare.ui.common.InputBoxPlaceholder
import com.SeniorCareMobileProject.seniorcare.ui.common.NavButton
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme

@Composable
fun LoginView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)

    ) {
        Column(Modifier.weight(128f)) {
            InputBoxPlaceholder()
        }
        Column(Modifier.weight(128f)) {
            val context = LocalContext.current
                Column{
//                    TextButton(
//                        onClick = {
//                            val intent = Intent(context, MapSampleActivity::class.java)
//                            context.startActivity(intent)
//                        },
//                        Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.CenterHorizontally)
//                            .fillMaxHeight()
//                            .wrapContentHeight(Alignment.CenterVertically)
//
//                    ) {
//                        //poniżej jest custom button który znajude się w folderze common
//                        CommonTemplateButton("Maps")
//                    }
                    NavButton(navController, buttonString = "Maps", route = "TemplateScreen")
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
