package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.MyApplication.Companion.context
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.body_16
import com.SeniorCareMobileProject.seniorcare.ui.theme.Main
import com.SeniorCareMobileProject.seniorcare.ui.theme.Red
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
fun ChooseRoleSection(sharedViewModel: SharedViewModel) {
    val firstPressed = remember { sharedViewModel.isFunctionCarer }
    val secondPressed = remember { sharedViewModel.isFunctionSenior }
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
fun InputField(
    placeholder: String,
    onValueChange: (String) -> Unit,
    viewModelVariable: MutableState<String>
) {
    val text = viewModelVariable.value

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
        onValueChange = { newText -> viewModelVariable.value = newText },
        placeholder = { Text(placeholder) },
        textStyle = TextStyle(
            fontSize = 14.sp
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordInputField(
    placeholder: String,
    onValueChange: (String) -> Unit,
    viewModelVariable: MutableState<String>
) {
    val text = viewModelVariable.value
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

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
        onValueChange = { newText -> viewModelVariable.value = newText },
        placeholder = { Text(placeholder) },
        textStyle = TextStyle(
            fontSize = 14.sp
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisibility)
                Icons.Default.Visibility
            else Icons.Default.VisibilityOff

            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    imageVector = image, contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )

                IconButton(onClick = {passwordVisibility = !passwordVisibility}){
                    Icon(imageVector  = image, null)
                }
            }
        }
    )
}

@Composable
fun InputFieldLabelIcon(
    text: String,
    onValueChange: (String) -> Unit,
    fieldLabel: String,
    iconName: String,
    viewModelVariable: MutableState<String>,
    isPassword: Boolean = false
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

        if (!isPassword) {
            InputField(text, onValueChange, viewModelVariable)

        } else {
            PasswordInputField(text, onValueChange, viewModelVariable)
        }

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

@Composable
fun RoundSmallButtonDrawer(
    navController: NavController,
    iconName: String,
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed)),
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Box(
        modifier = Modifier
            .width(46.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFCAAAF9))
            .clickable {
                scope.launch { scaffoldState.drawerState.open() }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun RoundSmallButton(
    navController: NavController,
    iconName: String,
    rout: String
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }
    Box(
        modifier = Modifier
            .width(46.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFCAAAF9))
            .clickable { navController.navigate(rout) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun RoundSmallButtonBack(
    navController: NavController,
    iconName: String,
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }
    Box(
        modifier = Modifier
            .width(46.dp)
            .height(46.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFFCAAAF9))
            .clickable { navController.popBackStack() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = iconName,
                tint = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun TopBarLocation(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Column(modifier = Modifier.wrapContentSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 8.dp, bottom = 8.dp)
                .padding(horizontal = 8.dp)
//                .clickable {
//                    scope.launch { scaffoldState.drawerState.open() }
//                }
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundSmallButtonDrawer(
                    navController = navController,
                    iconName = "menu",
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                RoundSmallButton(
                    navController = navController,
                    iconName = "settings",
                    rout = "CarerSettingsListScreen"
                )
            }
        }
    }
}

@Composable
fun TopBar(
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TopAppBar(backgroundColor = Color(0xFFCAAAF9)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
//                .padding(top = 11.dp, bottom = 11.dp)
//                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
//                .clickable {
//                    scope.launch { scaffoldState.drawerState.open() }
//                }
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundSmallButtonDrawer(
                    navController = navController,
                    iconName = "menu",
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            }

            Text(
                text = "Grzegorz Brzęczyszczykiewicz",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                RoundSmallButton(
                    navController = navController,
                    iconName = "settings",
                    rout = "CarerSettingsListScreen"
                )
            }
        }
    }
}

@Composable
fun TopBarSettings(
    navController: NavController,
    text: String
) {
    TopAppBar(backgroundColor = Color(0xFFCAAAF9)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundSmallButtonBack(
                    navController = navController,
                    iconName = "keyboard_backspace",
                )
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun SettingsItem(
    navController: NavController,
    text: String,
    rout: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                if (rout != "") navController.navigate(rout) else inProgressToastView(
                    context
                )
            }
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = 16.dp),
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Start,
            lineHeight = 24.sp,
            style = TextStyle(
                fontSize = 16.sp
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Divider(color = Color(0xffe6e6e6))
        }
    }
}

@Composable
fun AddNumber(
    navController: NavController,
    text: String,
    setShowDialog: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { setShowDialog(true) }
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(top = 16.dp),
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Start,
            lineHeight = 24.sp,
            style = TextStyle(
                fontSize = 16.sp
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Divider(color = Color(0xffe6e6e6))
        }
    }
}

@Composable
fun SettingsItemWithIcon(
    navController: NavController,
    text: String,
    rout: String,
    iconName: String,
) {
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = text,
                color = Color.Black,
                textAlign = TextAlign.Start,
                lineHeight = 24.sp,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { navController.navigate(rout) }
                )
            }
        }

//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Bottom
//        ) {
//            Divider(color = Color(0xffe6e6e6))
//        }
    }
}


@Composable
fun SettingsNumberElement(
    name: String,
    number: Int,
    edit: Boolean
) {
    val textName = remember { mutableStateOf(TextFieldValue(name)) }
    val textNumber = remember { mutableStateOf(TextFieldValue(number.toString())) }


    if (!edit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = number.toString(),
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        lineHeight = 24.sp,
                        style = TextStyle(
                            fontSize = 16.sp
                        )
                    )
                }

            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.width(240.dp),
                    value = textName.value,
                    onValueChange = { textName.value = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    TextField(
                        value = textNumber.value,
                        onValueChange = { textNumber.value = it },
                        textStyle = TextStyle(
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFF5F5F5),
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        }
    }


}

@Composable
fun StatusWidget(navController: NavController, title: String, text: String, iconName: String) {
    val widgetContentColor = Color(0xFF666666)
    val context = LocalContext.current
    val iconId = remember(iconName) {
        context.resources.getIdentifier(
            iconName,
            "drawable",
            context.packageName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(20.dp),
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 14.dp, bottom = 14.dp)
                .padding(horizontal = 26.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = widgetContentColor
                )
                Text(text = text, fontSize = 12.sp, color = widgetContentColor)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = iconName,
                    tint = widgetContentColor,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MedicalDataItem(title: String, text: String) {
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
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = text, fontSize = 14.sp)
        }
        Divider(thickness = 1.dp, color = Color(0xFFF5F5F5))
    }
}

@Composable
fun MedicalDataItemUpd(title: String, text: String) {
//    val titleRem = remember { mutableStateOf(TextFieldValue(title)) }
    val textRem = remember { mutableStateOf(TextFieldValue(text)) }

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
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textRem.value,
                onValueChange = { textRem.value = it },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF5F5F5),
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
//            BasicTextField(
//                value = textRem.value,
//                onValueChange = { textRem.value = it },
//            )

//            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)

//            Text(text = text, fontSize = 14.sp)
        }
        Divider(thickness = 1.dp, color = Color(0xFFF5F5F5))
    }
}

@Composable
fun NotificationItem(title: String, howOften: String, listOfTime: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(70f)) {
                Text(text = title)

            }

            Column(
                modifier = Modifier
                    .weight(30f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(howOften + ":")
                listOfTime.forEach { item ->
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
fun FloatingButton() {
    val context = LocalContext.current

    FloatingActionButton(
        modifier = Modifier.size(48.dp),
        onClick = { inProgressToastView(context) },
        backgroundColor = Color.Blue,
    ) {
        Icon(
            Icons.Filled.Add, "",
            modifier = Modifier.size(48.dp),
            tint = Color.White
        )
    }
}

@Composable
fun PopupButton(text: String) {
    Button(
        onClick = { },
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff2954ef))
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
    }
}

fun inProgressToastView(context: Context) {
    Toast.makeText(context, "W budowie", Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SignUpViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
//        val sharedViewModel = SharedViewModel()

        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val listOfTime = listOf<String>("10:00", "15:00", "20:00")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
//            EditButton()
//            DeleteButton()

//            SmallButton("Text", mutableStateOf(false))
//            ChooseRoleSection(sharedViewModel)
//            IconTextButton(navController, text = "Text", iconName = "google", "")
//            InputField(placeholder = "Text", onValueChange = {}, viewModelVariable = mutableStateOf(""))
//            InputFieldLabelIcon(
//                text = "Text",
//                onValueChange = {},
//                fieldLabel = "Label",
//                "alternate_email",
//                viewModelVariable = remember { mutableStateOf("") }
//            )
            InputFieldLabelIcon(
                text = "Text",
                onValueChange = {},
                fieldLabel = "Label",
                "",
                viewModelVariable = remember { mutableStateOf("") }
            )
            InputFieldLabelIcon(
                text = "Text",
                onValueChange = {},
                fieldLabel = "Label",
                "",
                viewModelVariable = remember { mutableStateOf("") },
                isPassword = true
            )

//            TextFilledButton(navController, "Text", "")
//            UserCard(
//                navController = navController,
//                name = "Piotr Kowalski",
//                emial = "piotr.kowalski@gmail.com"
//            )
//            Input()
            RoundSmallButton(
                navController = navController,
                iconName = "menu",
                rout = ""
            )
            TopBarSettings(navController = navController, "Ustawienia - Piotr Kowalski")
            SettingsItem(navController, "Przycisk SOS", "")
            SettingsItemWithIcon(navController, "Przycisk SOS", "", "edit")
            SettingsNumberElement("Paweł", 123456789, false)
            SettingsNumberElement("Paweł", 123456789, true)
//            StatusWidget(
//                navController, title = "Najbliższe wydarzenie:",
//                text = "Wizyta u lekarza\nData: 12.05.22 - godzina: 08:00",
//                iconName = "calendar_month"
//            )
            TopBar(navController, scope, scaffoldState)
//            MedicalDataItem(
//                "Przyjmowane leki:",
//                "Donepezil (50mg dwa razy dziennie)\n" + "Galantamin (25mg trzy razy dziennie)"
//            )
//            MedicalDataItemUpd(
//                "Przyjmowane leki:",
//                "Donepezil (50mg dwa razy dziennie)\n" + "Galantamin (25mg trzy razy dziennie)"
//            )
//            NotificationItem(title = "IBUPROM", howOften = "Codziennie", listOfTime = listOfTime)
//            FloatingButton()
            PopupButton("Text")
        }

    }
}