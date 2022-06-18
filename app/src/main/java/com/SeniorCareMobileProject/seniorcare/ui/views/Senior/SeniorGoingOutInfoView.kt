package com.SeniorCareMobileProject.seniorcare.ui.views.Senior

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Atoms.SeniorButtonNoIcon
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView


@Composable
fun SeniorGoingOutInfoView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF1ECF8))
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clickable { navController.navigate(NavigationScreens.SeniorMainScreen.name) }
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 112.dp),
                ) {
                    Text(
                        text = "Poinformowaliśmy twojego opiekuna o wyjściu z domu. Proszę wciśnij poniższy przycisk w momencie, gdy wrócisz do swojego domu.",
                        fontSize = 28.sp, fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))


            SeniorButtonNoIcon(
                navController = navController,
                text = "Wróciłem do domu",
                rout = "SeniorMainScreen",
                color = "came_home"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SeniorGoingOutInfoViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        SeniorGoingOutInfoView(navController)
    }
}