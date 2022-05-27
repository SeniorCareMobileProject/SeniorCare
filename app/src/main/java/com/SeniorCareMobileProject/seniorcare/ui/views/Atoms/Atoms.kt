package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.SeniorCareMobileProject.seniorcare.ui.body_16
import com.SeniorCareMobileProject.seniorcare.ui.theme.*
import com.example.seniorcare.R
import org.w3c.dom.Text

//class Atoms {
@Preview
@Composable
fun EditButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(backgroundColor = Main)
    ) {
        Text(
            text = "Edytuj",
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 8.sp
            )
        )
    }
}

@Preview
@Composable
fun DeleteButton() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(backgroundColor = Red)
    ) {
        Text(
            text = "Usuń",
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 8.sp
            )
        )
    }
}

@Preview
@Composable
fun SmallButtonUnpressed() {
    Button(
        onClick = { },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .width(width = 149.dp)
            .height(height = 30.dp)
    ) {
        Text(
            text = "Text",
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

@Preview
@Composable
fun SmallButtonPressed() {
    Button(
        onClick = { },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff3d1574)),
        modifier = Modifier
            .width(width = 149.dp)
            .height(height = 30.dp)
    ) {
        Text(
            text = "Text",
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

//@Preview
@Composable
fun IconTextButton(text: String, iconName: String) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Button(
        onClick = { },
        border = BorderStroke(1.5.dp, Color.Black),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(56.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = iconName,
            tint = Color.Unspecified
        )
        Spacer(
            modifier = Modifier
                .width(width = 8.dp)
        )
        Text(
            text = text,
            color = Color(0xff070707),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            style = body_16
        )
    }
}

//}