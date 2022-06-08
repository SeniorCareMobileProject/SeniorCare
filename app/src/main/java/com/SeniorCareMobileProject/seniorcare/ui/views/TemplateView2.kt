package com.SeniorCareMobileProject.seniorcare.ui.views

import android.text.Layout
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.common.CommonTemplateButton
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.R


@Composable
fun TemplateView2(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .fillMaxHeight()

    ) {
        TemplateText2()
        TemplateNavButton2(navController)
    }
}

@Composable
fun TemplateText2() {
    Text(
        text = "This text also shouldn't be hardcoded",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically),
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    )
}

@Composable
fun TemplateNavButton2(navController: NavController){
    TextButton(
        onClick = {
            //navController.navigate("TemplateScreen2")
            navController.popBackStack()
        },
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.Bottom)

    ) {
        CommonTemplateButton(stringResource(R.string.template_string2))
    }
}




@Preview(showBackground = true)
@Composable
fun TemplateView2Preview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val ebookSelectionViewModel = SharedViewModel()
        TemplateView(navController, ebookSelectionViewModel)
    }
}