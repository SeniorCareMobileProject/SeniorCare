package com.SeniorCareMobileProject.seniorcare.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.SeniorCareMobileProject.seniorcare.R

@Composable
fun NavButton(navController: NavController, buttonString: String, route: String){
    Column{
        TextButton(
            onClick = {
                navController.navigate(route)
            },
            Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .fillMaxHeight()
                .wrapContentHeight(Alignment.CenterVertically)

        ) {
            //poniżej jest custom button który znajude się w folderze common
            CommonTemplateButton(buttonString)
        }
    }
}