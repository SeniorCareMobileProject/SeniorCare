package com.wakeupgetapp.seniorcare.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.common.CommonTemplateButton
import com.wakeupgetapp.seniorcare.ui.common.MapWindowComponent
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
import com.wakeupgetapp.seniorcare.R

@Composable
fun TemplateView(navController: NavController, sharedViewModel: SharedViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .fillMaxHeight()

    ) {
        Column(Modifier.weight(8f)) {
            TemplateText()
        }
        Column(Modifier.weight(20f)) {
            MapWindowComponent(sharedViewModel)
        }
        Column(Modifier.weight(8f)) {
            TemplateNavButton(navController)
        }


    }
}

@Composable
fun TemplateText() {
    Text(
        text = "This text shouldn't be hardcoded",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically),
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    )
}

@Composable
fun TemplateNavButton(navController: NavController){
    TextButton(
        onClick = {
            navController.navigate("TemplateScreen2")
        },
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.Bottom)

    ) {
        //poniżej jest custom button który znajude się w folderze common
        CommonTemplateButton(stringResource(R.string.template_string))
    }
}




@Preview(showBackground = true)
@Composable
fun TemplateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val ebookSelectionViewModel = SharedViewModel()
        TemplateView(navController, ebookSelectionViewModel)
    }
}