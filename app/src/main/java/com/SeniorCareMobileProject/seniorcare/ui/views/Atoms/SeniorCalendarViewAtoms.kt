package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.TemplateView3
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import com.SeniorCareMobileProject.seniorcare.ui.views.Carer.PairingScreenCodeView
import kotlinx.datetime.LocalTime


@Composable
fun DaySeparator(text: String) {
    Column(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(Color(0xFFCAAAF9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 28.sp)
    }
}

@Composable
fun SeniorCalendarEventItemView(
    startTime: LocalTime,
    endTime: LocalTime,
    eventName: String,
    eventDescription: String
) {
    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .height(height = 126.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                start = 21.dp,
                end = 10.dp,
                top = 27.dp,
                bottom = 34.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = eventName,
                    color = Color(0xff070707),
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                if (eventDescription.isNotBlank()) {
                    Text(
                        text = eventDescription,
                        color = Color(0xff8c8c8c),
                        lineHeight = 24.sp,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$startTime - $endTime",
                    color = Color(0xff070707),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SeniorCalendarViewAtomsPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
//        DaySeparator(navController, "Dzisiaj")
//        SeniorCalendarEventItemView(
//            LocalTime(16, 0, 0),
//            LocalTime(16, 30, 0),
//            "Example",
//            "Description of the test event"
//        )
    }
}