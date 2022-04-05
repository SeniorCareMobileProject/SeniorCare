package com.SeniorCareMobileProject.seniorcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.TemplateView
import com.SeniorCareMobileProject.seniorcare.ui.views.TemplateView2

class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            SeniorCareTheme() {
                val navController = rememberNavController()
                NavHost(
                    navController,
                    startDestination = NavigationScreens.TemplateScreen.name,
                ) {

                    composable(NavigationScreens.TemplateScreen.name) {
                        TemplateView(navController, sharedViewModel)
                    }

                    composable(NavigationScreens.TemplateScreen2.name) {
                        TemplateView2(navController, sharedViewModel)

                    }

                }

            }
        }
    }
}

