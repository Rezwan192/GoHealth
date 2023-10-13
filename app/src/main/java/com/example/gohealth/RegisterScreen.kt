package com.example.gohealth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository


@Composable
fun Register(navController: NavHostController) {
    val passwordFocusRequester = FocusRequester()
    val focusManager:FocusManager = LocalFocusManager.current
    val patientRepository = PatientRepository()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF0260A8)) //color = colorResource(id = R.color.my_primary))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Login Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(topEnd = 75.dp, bottomStart = 75.dp))
            )
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background //Color.White,
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Please enter your name, email address and password.",
                        modifier = Modifier.padding(top = 20.dp, bottom = 14.dp),
                        textAlign = TextAlign.Center
                    )
                    TextInput(
                        value = firstName,
                        com.example.gohealth.OutputType.First,
                        keyboardActions = KeyboardActions(onNext = {
                            passwordFocusRequester.requestFocus()
                        }),
                        onValueChange = { firstName = it }
                    )
                    TextInput(
                        value = lastName,
                        com.example.gohealth.OutputType.Last,
                        keyboardActions = KeyboardActions(onNext = {
                            passwordFocusRequester.requestFocus()
                        }),
                        onValueChange = { lastName = it }
                    )
                    TextInput(
                        value = email,
                        com.example.gohealth.OutputType.Email,
                        keyboardActions = KeyboardActions(onNext = {
                            passwordFocusRequester.requestFocus()
                        }),
                        onValueChange = { email = it }
                    )
                    TextInput(
                        value = password,
                        com.example.gohealth.OutputType.Password,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        focusRequester = passwordFocusRequester,
                        onValueChange = { password = it }
                    )

                    Button(
                        onClick = {
                            // add new patient to firestore
                            val newPatient = Patient(
                                firstName = firstName,
                                lastName = lastName,
                                email = email
                            )
                            patientRepository.addPatient(newPatient)
                            navController.navigate("login")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer),//colorResource(id = R.color.my_primary)),
                    ) {
                        Text(
                            text = "Register",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onBackground, //Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Have an account?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        TextButton(onClick = {
                            navController.navigate("login")
                        }) {
                            Text(
                                text ="Login here",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary) //colorResource(id = R.color.my_primary))
                        }
                    }
                }
            }
        }
    }
}

sealed class OutputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOperations: KeyboardOptions,
    val visualTransformation: VisualTransformation,
) {

    object First: com.example.gohealth.OutputType(
        label = "First Name",
        icon = Icons.Default.AccountCircle,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Last: com.example.gohealth.OutputType(
        label = "Last Name",
        icon = Icons.Default.AccountCircle,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Email: com.example.gohealth.OutputType(
        label = "Email Address",
        icon = Icons.Default.Email,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Password: com.example.gohealth.OutputType(
        label = "Password",
        icon = Icons.Default.Lock,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    value: String,
    inputType: com.example.gohealth.OutputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .focusRequester(focusRequester ?: FocusRequester()),
        leadingIcon = { Icon(imageVector = inputType.icon, contentDescription = null) },
        label = { Text(text = inputType.label)},
        shape = RoundedCornerShape(32.dp),
        singleLine = true,
        keyboardOptions = inputType.keyboardOperations,
        visualTransformation = inputType.visualTransformation,
        keyboardActions = keyboardActions
    )
}

//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "DefaultPreviewDark"
//)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//    name = "DefaultPreviewLight"
//)
//
//@Composable
//fun RegisterScreenPreview() {
//    GoHealthTheme {
//        Register()
//    }
//}

