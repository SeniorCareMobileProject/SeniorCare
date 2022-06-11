package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme

@Composable
fun SeniorButton(
    navController: NavController,
    text: String,
    iconName: String,
    rout: String,
    color: String
) {
    val backgroundColor: Color

    backgroundColor = when (color) {
        "main" -> {
            Color(0xffcaaaf9)
        }
        "red" -> {
            Color.Red
        }
        else -> {
            Color.White
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(86.dp)
            .clickable { navController.navigate(rout) },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.Black),
        color = backgroundColor
    ) {
        val context = LocalContext.current
        val iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = text,
                    color = Color(0xff070707),
                    textAlign = TextAlign.Start,
                    lineHeight = 36.sp,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = Color.Black
                )
            }

        }
    }
}

@Composable
fun SeniorMedicalDataItem(title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 5.dp)
        ) {
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Medium)
            Text(text = text, fontSize = 28.sp)
        }
        Divider(thickness = 1.dp, color = Color(0xFFE6E6E6))
    }
}


@Preview(showBackground = true)
@Composable
fun TemplateViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        Column() {
            SeniorButton(
                navController,
                "Wychodzę z domu",
                "my_location",
                "",
                "main"
            )
            SeniorMedicalDataItem(
                title = "Przyjmowane leki:",
                text = "Donepezil (50mg dwa razy dziennie)\nGalantamin (25mg trzy razy dziennie)"
            )
        }
    }
}

