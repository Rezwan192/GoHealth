package com.example.gohealth

import android.util.Log
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import com.example.gohealth.data.User
import com.example.gohealth.data.UserRepository
import com.example.gohealth.data.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern
import kotlinx.coroutines.launch


@Composable
fun Register(navController: NavHostController) {
    val passwordFocusRequester = FocusRequester()
    val focusManager:FocusManager = LocalFocusManager.current
    val patientRepository = PatientRepository()
    val userRepository = UserRepository()
    val coroutineScope = rememberCoroutineScope()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordPolicyDialogOpen by remember { mutableStateOf(false) }

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
                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 15.sp
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
                            isPasswordPolicyDialogOpen = true
                        },
                        modifier = Modifier.padding(top = 11.dp)
                    ) {
                        Text("Password Policy")
                    }

                    if (isPasswordPolicyDialogOpen) {
                        AlertDialog(
                            onDismissRequest = {
                                isPasswordPolicyDialogOpen = false
                            },
                            title = {
                                Text("Password Policy",)
                            },
                            text = {
                                BulletList(
                                    "At least one digit (0-9)",
                                    "At least one lowercase letter (a-z)",
                                    "At least one uppercase letter (A-Z)",
                                    "At least one special character from the set @#$%^&+=!",
                                    "A minimum length of 8 characters"
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        isPasswordPolicyDialogOpen = false
                                    }
                                ) {
                                    Text("Close")
                                }
                            }
                        )
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    // Check the password strength before attempting user creation
                                    if (!isStrongPassword(password)) {
                                        errorMessage = "Password does not meet criteria of password policy."
                                        isPasswordPolicyDialogOpen = true
                                        val error = FirebaseAuthException(
                                            "ERROR_WEAK_PASSWORD",
                                            "Password does not meet criteria of password policy."
                                        )
                                        Log.e("FirebaseAuth", "Registration error: ${error.message}", error)
                                        throw error
                                    }

                                    // Create user in Firebase Authentication
                                    val authResult = FirebaseAuth.getInstance()
                                        .createUserWithEmailAndPassword(email, password).await()

                                    // Get the UID of the newly created user
                                    val uid = authResult.user?.uid ?: throw Exception("User not found")

                                    // Add new user with Patient role to Firestore
                                    val newUser = User(uid, email = email, UserRole.PATIENT)
                                    userRepository.addUser(newUser)

                                    // Add new patient to Firestore
                                    val newPatient = Patient(
                                        userId = uid,
                                        firstName = firstName,
                                        lastName = lastName,
                                        email = email
                                    )
                                    patientRepository.addPatient(newPatient)


                                    navController.navigate("login")

                                } catch (error: FirebaseAuthInvalidCredentialsException) {
                                    // Handle invalid email
                                    errorMessage = "Please enter a valid email address"
                                    Log.e("FirebaseAuth", "Registration error: ${error.message}", error)
                                } catch (error: FirebaseAuthUserCollisionException) {
                                    // Handle email collision
                                    errorMessage = "A user with this email address already exists"
                                    Log.e("FirebaseAuth", "Registration error: ${error.message}", error)
                                } catch (error: FirebaseAuthException) {
                                    // Handle other FirebaseAuth exceptions
                                    errorMessage = error.message ?: "Registration failed: An error occurred. A"
                                    Log.e("FirebaseAuth", "Registration error: ${error.message}", error)
                                } catch (error: Exception) {
                                    // Handle other general exceptions
                                    errorMessage = error.message ?: "Registration failed: An error occurred. B"
                                    Log.e("FirebaseAuth", "Registration error: ${error.message}", error)
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

fun isStrongPassword(password: String): Boolean {
    val passwordPattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    )
    return passwordPattern.matcher(password).matches()
}

class WeakPasswordException : Exception("Password does not meet the required criteria of the password policy.")

@Composable
fun BulletList(vararg items: String) {
    Column {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(4.dp)
            ) {
                Text("•", modifier = Modifier.padding(end = 4.dp))
                Text(item)
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

