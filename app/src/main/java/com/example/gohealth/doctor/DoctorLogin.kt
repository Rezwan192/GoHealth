package com.example.gohealth.doctor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gohealth.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DoctorLogin(navController: NavHostController) {
    val passwordFocusRequester = FocusRequester()
    val focusManager: FocusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF12CEAC))
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
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 15.dp, bottom = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "GoHealth",
                        color = MaterialTheme.colorScheme.primary, //colorResource(id = R.color.my_primary),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Please enter your email address and password.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 20.dp, bottom = 14.dp)
                    )
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 15.sp
                    )
                    TextInput(
                        value = email,
                        DocType.Email,
                        keyboardActions = KeyboardActions(onNext = {
                            passwordFocusRequester.requestFocus()
                        }),
                        onValueChange = { email = it }
                    )
                    TextInput(
                        value = password,
                        DocType.Password,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        }),
                        focusRequester = passwordFocusRequester,
                        onValueChange = { password = it }
                    )

                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate("doctorhome")
                                    } else {
                                        val exception = task.exception
                                        if (exception is com.google.firebase.FirebaseException) {
                                            if (exception.message?.contains("INVALID_LOGIN_CREDENTIALS") == true) {
                                                errorMessage = "Incorrect email or password."
                                            } else {
                                                errorMessage = "Login failed: An error occurred."
                                            }
                                        } else {
                                            errorMessage = "Login failed: An error occurred."
                                        }
                                        Log.e("FirebaseAuth", "Login error: ${exception?.message}", exception)
                                    }
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer),//colorResource(id = R.color.my_primary)),
                    ) {
                        Text(
                            text = "Login",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

sealed class DocType(
    val label: String,
    val icon: ImageVector,
    val keyboardOperations: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Email: DocType(
        label = "Email Address",
        icon = Icons.Default.Email,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Password: DocType(
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
    inputType: DocType,
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

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TextInput(
//    inputType: DocType,
//    focusRequester: FocusRequester? = null,
//    keyboardActions: KeyboardActions
//) {
//
//    var text:String by remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = text,
//        onValueChange = { text = it },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 15.dp)
//            .focusRequester(focusRequester ?: FocusRequester()),
//        leadingIcon = { Icon(imageVector = inputType.icon, contentDescription = null) },
//        label = { Text(text = inputType.label) },
//        shape = RoundedCornerShape(32.dp),
//        singleLine = true,
//        keyboardOptions = inputType.keyboardOperations,
//        visualTransformation = inputType.visualTransformation,
//        keyboardActions = keyboardActions
//    )
//}