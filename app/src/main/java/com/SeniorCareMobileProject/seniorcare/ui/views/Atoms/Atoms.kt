package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.body_16
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.*
import com.example.seniorcare.R

//@Preview
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

//@Preview
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

//@Preview
@Composable
fun SmallButton(text: String, isPressed: MutableState<Boolean>) {
    val color = if (isPressed.value) Color(0xff3D1574) else Color(0xff7929e8)

    Button(
        onClick = { isPressed.value = !isPressed.value },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        modifier = Modifier
            .width(width = 187.dp)
            .height(height = 30.dp)
            .padding(horizontal = 19.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun SmallButtonNoPressedState(text: String) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .width(width = 187.dp)
            .height(height = 30.dp)
            .padding(horizontal = 19.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun SmallButtonWithRout(navController: NavController, text: String, rout: String) {
    Button(
        onClick = { navController.navigate(rout) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .width(width = 187.dp)
            .height(height = 30.dp)
            .padding(horizontal = 19.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun ChooseRoleSection() {
    val firstPressed = remember { mutableStateOf(false) }
    val secondPressed = remember { mutableStateOf(false) }
    val lastPressed = remember { mutableStateOf(-1) }

    if (lastPressed.value.compareTo(0) == 0 && firstPressed.value && !secondPressed.value) {
        firstPressed.value = true
        secondPressed.value = false
        lastPressed.value = 0
    } else if (lastPressed.value.compareTo(0) == 0 && !firstPressed.value && secondPressed.value) {
        firstPressed.value = false
        secondPressed.value = true
        lastPressed.value = 1
    } else if (lastPressed.value.compareTo(1) == 0 && !firstPressed.value && secondPressed.value) {
        firstPressed.value = false
        secondPressed.value = true
        lastPressed.value = 1
    } else if (lastPressed.value.compareTo(1) == 0 && firstPressed.value && !secondPressed.value) {
        firstPressed.value = true
        secondPressed.value = false
        lastPressed.value = 0
    } else if (lastPressed.value.compareTo(0) == 0 && firstPressed.value && secondPressed.value) {
        firstPressed.value = false
        secondPressed.value = true
        lastPressed.value = 1
    } else if (lastPressed.value.compareTo(1) == 0 && firstPressed.value && secondPressed.value) {
        firstPressed.value = true
        secondPressed.value = false
        lastPressed.value = 0
    } else if (lastPressed.value.compareTo(-1) == 0) {
        if (firstPressed.value) {
            lastPressed.value = 0
        } else {
            lastPressed.value = 1
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = "Rola",
            fontWeight = FontWeight.Medium
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            SmallButton(text = "Opiekun", firstPressed)
            SmallButton(text = "Podopieczny", secondPressed)
        }
    }
}

@Composable
fun IconTextButton(navController: NavController, text: String, iconName: String, rout: String) {
    var iconId = -1
    if (!iconName.equals("")) {
        val context = LocalContext.current
        iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }
    }

    Button(
        onClick = { navController.navigate(rout) },
        border = BorderStroke(1.5.dp, Color.Black),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (!iconId.equals(-1)) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Unspecified
            )
        }
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

@Composable
fun Input() {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clear),
            contentDescription = "Clear"
        )
        Spacer(
            modifier = Modifier
                .width(width = 8.dp)
        )
        Text(
            text = "Placeholder_input",
            color = Color(0xff8c8c8c),
            textAlign = TextAlign.Start,
            lineHeight = 20.sp,
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun InputField(placeholder: String, onValueChange: (String) -> Unit) {
    var text by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White,
            disabledIndicatorColor = MaterialTheme.colors.primary,
            unfocusedIndicatorColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = { newText -> text = newText },
        placeholder = { Text(placeholder) },
        textStyle = TextStyle(
            fontSize = 14.sp
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun InputFieldLabelIcon(
    text: String,
    onValueChange: (String) -> Unit,
    fieldLabel: String,
    iconName: String
) {
    var iconId = -1

    if (iconName != "") {
        val context = LocalContext.current
        iconId = remember(iconName) {
            context.resources.getIdentifier(
                iconName,
                "drawable",
                context.packageName
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = fieldLabel)
            if (iconId > -1) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        InputField(text, onValueChange)

    }
}

@Composable
fun TextFilledButton(navController: NavController, text: String, rout: String) {
    Button(
        onClick = { navController.navigate(rout) },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff7929e8)),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun UserCard(navController: NavController, name: String, emial: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .height(76.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .wrapContentWidth()
            )
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .padding(horizontal = 30.dp)
                    )
                    Text(
                        text = emial,
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .padding(bottom = 14.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            EditButton()
            DeleteButton()
            SmallButton("Text", mutableStateOf(false))
            ChooseRoleSection()
            IconTextButton(navController, text = "Text", iconName = "google", "")
            InputField(placeholder = "Text", onValueChange = {})
            InputFieldLabelIcon(
                text = "Text",
                onValueChange = {},
                fieldLabel = "Label",
                "alternate_email"
            )
            InputFieldLabelIcon(
                text = "Text",
                onValueChange = {},
                fieldLabel = "Label",
                ""
            )
            TextFilledButton(navController, "Text", "")
            UserCard(
                navController = navController,
                name = "Piotr Kowalski",
                emial = "piotr.kowalski@gmail.com"
            )
            Input()
        }

    }
}