package com.SeniorCareMobileProject.seniorcare.ui.views.Atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.SeniorCareMobileProject.seniorcare.ui.SharedViewModel
import com.SeniorCareMobileProject.seniorcare.ui.navigation.NavigationScreens
import com.SeniorCareMobileProject.seniorcare.ui.theme.SeniorCareTheme


@Composable
fun NewContactPopupView(setShowDialog: (Boolean) -> Unit, sharedViewModel: SharedViewModel) {
    Dialog(onDismissRequest = { setShowDialog(false) },
    ) {
        Surface(
            modifier = Modifier
                .width(360.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFF1ECF8)
        ) {
            val contactName = remember { mutableStateOf(TextFieldValue("")) }
            val contactNumber = remember { mutableStateOf(TextFieldValue("")) }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TextField(
                    value = contactName.value,
                    onValueChange = { contactName.value = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(text = "Nazwa kontaktu") }
                )
                TextField(
                    value = contactNumber.value,
                    onValueChange = { contactNumber.value = it },
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = { Text(text = "Numer telefonu") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PopupButton("Anuluj",setShowDialog)
                    PopupButtonAddNumber(text = "Dodaj", sharedViewModel = sharedViewModel, name = contactName.value.text, number = contactNumber.value.text, setShowDialog)

                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
fun NewContactPopupViewPreview() {
    SeniorCareTheme() {
        val navController = rememberNavController()
        val showDialog =  remember { mutableStateOf(false) }

        //NewContactPopupView( setShowDialog = { showDialog.value = it } )
    }
}