package com.wakeupgetapp.seniorcare.ui.views.Atoms

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wakeupgetapp.seniorcare.MyApplication.Companion.context
import com.wakeupgetapp.seniorcare.R
import com.wakeupgetapp.seniorcare.data.emptyEvent
import com.wakeupgetapp.seniorcare.ui.SharedViewModel
import com.wakeupgetapp.seniorcare.ui.body_16
import com.wakeupgetapp.seniorcare.ui.theme.Main
import com.wakeupgetapp.seniorcare.ui.theme.Red
import com.wakeupgetapp.seniorcare.ui.theme.SeniorCareTheme
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
    val color = if (isPressed.value) Color(0xff7929e8) else Color(0xff8C8C8C)

    Button(
        onClick = { isPressed.value = !isPressed.value },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        modifier = Modifier
            .width(width = 187.dp)
            .heightIn(min = 30.dp)
            .wrapContentHeight()
            .padding(horizontal = 19.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
            .padding(top = 12.dp)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = context!!.getString(R.string.role),
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            SmallButton(text = stringResource(R.string.carer), firstPressed)
            SmallButton(text = stringResource(R.string.senior), secondPressed)
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

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, null)
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
    scaffoldState: ScaffoldState,
    sharedViewModel: SharedViewModel
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
                text = "${sharedViewModel.currentSeniorData.value?.firstName} ${sharedViewModel.currentSeniorData.value?.lastName}",
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
    sharedViewModel: SharedViewModel
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
                text = context!!.getString(
                    R.string.top_bar_settings_senior_name,
                    sharedViewModel.currentSeniorData.value?.firstName,
                    sharedViewModel.currentSeniorData.value?.lastName
                ),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun SettingsItem(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    text: String,
    rout: String
) {
    val context = LocalContext.current

    val showDisconnectConfirmDialog = remember { mutableStateOf(false) }
    if (showDisconnectConfirmDialog.value) {
        SubmitOrDenyDialogView(
            context.getString(
                R.string.disconnect_confirmation,
                sharedViewModel.currentSeniorData.value!!.firstName,
                sharedViewModel.currentSeniorData.value!!.lastName
            ),
            { showDisconnectConfirmDialog.value = false },
            sharedViewModel,
            showDisconnectConfirmDialog,
            {

                sharedViewModel.disconnectWithSenior()
                Toast.makeText(
                    context,
                    context.getString(R.string.disconnected_with_user),
                    Toast.LENGTH_LONG
                ).show()

                if (sharedViewModel.haveConnectedUsers) {
                    navController.navigate("LoadingDataView") {
                        popUpTo("CarerSettingsListScreen") { inclusive = true }
                    }
                } else {
                    navController.navigate("CarerNoConnectedSeniorsView") {
                        popUpTo("CarerSettingsListScreen") { inclusive = true }
                    }
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                if (rout != "") {
                    navController.navigate(rout)
                } else {
                    showDisconnectConfirmDialog.value = true
                }
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
    sharedViewModel: SharedViewModel,
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
                        .clickable { navController.navigateUp();sharedViewModel.saveSosNumbersToFirebase() }
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
fun SOSSettingsItemWithIcon(
    navController: NavController,
    sharedViewModel: SharedViewModel,
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
            .clickable {
                navController.navigate(rout)
                sharedViewModel.saveSosNumbersToFirebase()
                if (rout == "CarerSettingsSOSScreen") {
                    navController.navigate(rout) {
                        popUpTo("CarerSettingsListScreen")
                    }
                }
                else {
                    navController.navigate(rout)
                }
            }
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
                horizontalAlignment = Alignment.Start
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
    index: Int,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    rout: String
) {
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
                .padding(end = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(4f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = sharedViewModel.sosPhoneNumbersNames[index],
                    color = Color.Black,
                    lineHeight = 24.sp,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }

            Column(
                modifier = Modifier
                    .weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = sharedViewModel.sosCascadePhoneNumbers[index],
                    color = Color.Black,
                    lineHeight = 24.sp,
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        sharedViewModel.sosPhoneNumbersNames.remove(sharedViewModel.sosPhoneNumbersNames[index])
                        sharedViewModel.sosCascadePhoneNumbers.remove(sharedViewModel.sosCascadePhoneNumbers[index])
                        sharedViewModel.sosSettingsNameStates.remove(sharedViewModel.sosSettingsNameStates[index])
                        sharedViewModel.sosSettingsNumberStates.remove(sharedViewModel.sosSettingsNumberStates[index])
                        sharedViewModel.saveSosNumbersToFirebase()
                        navController.navigate(rout)
                    },
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(
                        id = LocalContext.current.resources.getIdentifier(
                            "remove",
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "Remove",
                    tint = Color.Black,
                )
            }

        }
    }


}

@Composable
fun SettingsEditNumberElement(
    index: Int,
    sharedViewModel: SharedViewModel,
) {
    var textName by remember { sharedViewModel.sosSettingsNameStates[index] }
    var textNumber by remember { sharedViewModel.sosSettingsNumberStates[index] }


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(end = 16.dp)
        ) {

            TextField(
                modifier = Modifier.weight(3f),
                value = textName,
                onValueChange = {
                    textName = it;sharedViewModel.sosPhoneNumbersNames[index] = textName
                },
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFF5F5F5),
                    unfocusedIndicatorColor = Color.Transparent
                ),
                label = { Text(text = context!!.getString(R.string.carer_settings_item)) }
            )
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = textNumber,
                    onValueChange = {
                        textNumber = it;sharedViewModel.sosCascadePhoneNumbers[index] = textNumber
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text(text = context!!.getString(R.string.phone_number_settings_item)) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp)
                    .clickable {
                        try {
                            var buff = sharedViewModel.sosCascadePhoneNumbers[index]
                            sharedViewModel.sosCascadePhoneNumbers[index] =
                                sharedViewModel.sosCascadePhoneNumbers[index - 1]
                            sharedViewModel.sosCascadePhoneNumbers[index - 1] = buff
                            textNumber = sharedViewModel.sosCascadePhoneNumbers[index]
                            sharedViewModel.sosSettingsNumberStates[index - 1].value =
                                sharedViewModel.sosCascadePhoneNumbers[index - 1]

                            buff = sharedViewModel.sosPhoneNumbersNames[index]
                            sharedViewModel.sosPhoneNumbersNames[index] =
                                sharedViewModel.sosPhoneNumbersNames[index - 1]
                            sharedViewModel.sosPhoneNumbersNames[index - 1] = buff
                            textName = sharedViewModel.sosPhoneNumbersNames[index]
                            sharedViewModel.sosSettingsNameStates[index - 1].value =
                                sharedViewModel.sosPhoneNumbersNames[index - 1]

                        } catch (e: Exception) {

                        }

                    },
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(
                        id = LocalContext.current.resources.getIdentifier(
                            "sos_arrow_upward",
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "ArrowUpward",
                    tint = Color.Black,
                )


            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
                    .padding(bottom = 8.dp)
                    .clickable {
                        try {
                            var buff = sharedViewModel.sosCascadePhoneNumbers[index]
                            sharedViewModel.sosCascadePhoneNumbers[index] =
                                sharedViewModel.sosCascadePhoneNumbers[index + 1]
                            sharedViewModel.sosCascadePhoneNumbers[index + 1] = buff
                            textNumber = sharedViewModel.sosCascadePhoneNumbers[index]
                            sharedViewModel.sosSettingsNumberStates[index + 1].value =
                                sharedViewModel.sosCascadePhoneNumbers[index + 1]

                            buff = sharedViewModel.sosPhoneNumbersNames[index]
                            sharedViewModel.sosPhoneNumbersNames[index] =
                                sharedViewModel.sosPhoneNumbersNames[index + 1]
                            sharedViewModel.sosPhoneNumbersNames[index + 1] = buff
                            textName = sharedViewModel.sosPhoneNumbersNames[index]
                            sharedViewModel.sosSettingsNameStates[index + 1].value =
                                sharedViewModel.sosPhoneNumbersNames[index + 1]

                        } catch (e: Exception) {

                        }

                    },
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    painter = painterResource(
                        id = LocalContext.current.resources.getIdentifier(
                            "sos_arrow_downward",
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "ArrowDownward",
                    tint = Color.Black,
                )


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
fun MedicalDataItemUpd(title: String, text: String, sharedViewModel: SharedViewModel, index: Int) {
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

        when (index) {
            0 -> sharedViewModel.medInfo.value!!.firstName = textRem.value.text
            1 -> sharedViewModel.medInfo.value!!.lastName = textRem.value.text
            2 -> sharedViewModel.medInfo.value!!.birthday = textRem.value.text
            3 -> sharedViewModel.medInfo.value!!.illnesses = textRem.value.text
            4 -> sharedViewModel.medInfo.value!!.bloodType = textRem.value.text
            5 -> sharedViewModel.medInfo.value!!.allergies = textRem.value.text
            6 -> sharedViewModel.medInfo.value!!.medication = textRem.value.text
            7 -> sharedViewModel.medInfo.value!!.height = textRem.value.text
            8 -> sharedViewModel.medInfo.value!!.weight = textRem.value.text
            9 -> sharedViewModel.medInfo.value!!.languages = textRem.value.text
            10 -> sharedViewModel.medInfo.value!!.others = textRem.value.text
        }
    }
}

@Composable
fun NotificationItem(
    title: String,
    howOften: String,
    listOfTime: List<String>,
    sharedViewModel: SharedViewModel,
    index: Int,
    navController: NavController,
    rout: String
) {
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
            Column(modifier = Modifier.weight(60f)) {
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
            Column(modifier = Modifier
                .weight(10f)
                .clickable {
                    sharedViewModel.notificationItems.remove(sharedViewModel.notificationItems[index])
                    navController.navigate(rout)
                    sharedViewModel.notificationitemsLiveData.value =
                        sharedViewModel.notificationItems
                    sharedViewModel.saveNotificationsToFirebase()
                }) {
                Icon(
                    painter = painterResource(
                        id = LocalContext.current.resources.getIdentifier(
                            "remove",
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "Remove",
                    tint = Color.Black,
                )

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
fun FloatingButtonNotifications(sharedViewModel: SharedViewModel, showDialog: MutableState<Boolean>) {
    FloatingActionButton(
        onClick = {
            showDialog.value = true
//            sharedViewModel.createNewEvent.value = true
        },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .width(48.dp)
                .height(48.dp),
            tint = Color.White
        )
    }
}

@Composable
fun PopupButton(text: String, setShowDialog: (Boolean) -> Unit) {
    Button(
        onClick = { setShowDialog(false) },
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

@Composable
fun SubmitOrDenyDialogView(
    text: String,
    onDismissRequest: () -> Unit,
    sharedViewModel: SharedViewModel,
    showDialog: MutableState<Boolean>,
    onConfirmRequest: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            backgroundColor = Color(0xFFF1ECF8),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = text)
                Row(modifier = Modifier.padding(top = 12.dp)) {
                    Button(
                        modifier = Modifier
                            .weight(10f),
                        shape = RoundedCornerShape(10.dp),
                        onClick =
                        onConfirmRequest
//                            sharedViewModel.removeEvent.value = true
//                            //Due to the line above, will not show dialog, only enter
//                            // "if" statement in CarerCalendarView.kt file
//                            showDialog.value = true
//                            sharedViewModel.removeEventConfirmation.value = false
                        ,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFA670F0),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.yes))
                    }

                    Spacer(modifier = Modifier.weight(2f))

                    Button(
                        modifier = Modifier
                            .weight(10f),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            sharedViewModel.modifiedEvent = emptyEvent.copy()
                            sharedViewModel.removeEventConfirmation.value = false
                            showDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF3D1574),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = context.getString(R.string.no))
                    }
                }
            }
        }
    }
}

@Composable
fun PopupButtonAddNumber(
    text: String,
    sharedViewModel: SharedViewModel,
    name: String,
    number: String,
    setShowDialog: (Boolean) -> Unit
) {
    Button(
        onClick = {
            if (name != "" && number != "" && number.length == 9) {
                sharedViewModel.sosCascadePhoneNumbers.add(number)
                sharedViewModel.sosPhoneNumbersNames.add(name)
                sharedViewModel.sosSettingsNumberStates.add(mutableStateOf(sharedViewModel.sosCascadePhoneNumbers[sharedViewModel.sosCascadePhoneNumbers.size - 1]))
                sharedViewModel.sosSettingsNameStates.add(mutableStateOf(sharedViewModel.sosPhoneNumbersNames[sharedViewModel.sosPhoneNumbersNames.size - 1]))
                sharedViewModel.saveSosNumbersToFirebase()
                setShowDialog(false)
            }
        },
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

@Composable
fun SplashScreenWithLoading(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            elevation = 2.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcherplay),
                contentDescription = "App Icon",
                modifier = Modifier.fillMaxSize()
            )
        }
        PaddingValues(20.dp)
        Text(text, fontWeight = FontWeight.Light, fontSize = 20.sp)
        PaddingValues(20.dp)
        CircularProgressIndicator()
    }
}

fun inProgressToastView(context: Context) {
    Toast.makeText(context, context!!.getString(R.string.work_in_progress), Toast.LENGTH_SHORT)
        .show()
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SignUpViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val sharedViewModel = SharedViewModel()

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
            TopBarSettings(navController = navController, sharedViewModel = sharedViewModel)
            SettingsItem(navController, sharedViewModel, "Przycisk SOS", "")
            SettingsItemWithIcon(navController, sharedViewModel, "Przycisk SOS", "", "edit")
            SettingsNumberElement(0, sharedViewModel, navController, "CarerSettingsSOSScreen")
            SettingsNumberElement(1, sharedViewModel, navController, "CarerSettingsSOSScreen")

//            StatusWidget(
//                navController, title = "Najbliższe wydarzenie:",
//                text = "Wizyta u lekarza\nData: 12.05.22 - godzina: 08:00",
//                iconName = "calendar_month"
//            )
            TopBar(navController, scope, scaffoldState, sharedViewModel)
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
            //PopupButton("Text")
        }

    }
}