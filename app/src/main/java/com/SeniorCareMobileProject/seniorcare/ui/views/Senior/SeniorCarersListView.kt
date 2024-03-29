package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButton
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun SeniorCarersListView(navController: NavController, sharedViewModel: SharedViewModel) {
    Scaffold(topBar = {
        SeniorTopBar(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }) {
        val scrollState = remember { ScrollState(0) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 54.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Text(
                text = stringResource(id = R.string.carers_list),
                fontWeight = FontWeight.Medium,
                fontSize = 36.sp,
            )

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                sharedViewModel.listOfConnectedUsers.forEach { item ->
                    SeniorButton(
                        navController = navController,
                        text = item,
                        iconName = "",
                        rout = "",
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorCarersListViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SeniorCarersListView(navController, sharedViewModel = SharedViewModel())
    }
}