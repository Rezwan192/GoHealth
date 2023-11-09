package com.example.gohealth

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.navigation.NavHostController
import com.example.gohealth.ui.theme.GoHealthTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.gohealth.displayOneTapUI
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            GoHealthTheme {
                Nav()
            }
        }
    }
}

///@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login(navController: NavHostController) {
    val passwordFocusRequester = FocusRequester()
    val focusManager:FocusManager = LocalFocusManager.current
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showOneTapUI by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF0260A8))
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
                    containerColor = MaterialTheme.colorScheme.background
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp, bottom = 10.dp),
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
                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp)
                    )
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 15.sp
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
                    TextButton(onClick = {navController.navigate("forgot")}) {
                        Text(
                            text = "Forgot Password?",
                            color = MaterialTheme.colorScheme.primary//colorResource(id = R.color.my_primary)
                        )
                    }
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate("patienthome")
                                    } else {
                                        val exception = task.exception
                                        if (exception is com.google.firebase.FirebaseException) {
                                            if (exception.message?.contains("INVALID_LOGIN_CREDENTIALS") == true) {
                                                errorMessage = "Incorrect email or password."
                                            } else {
                                                errorMessage = "Login failed: An error occurred."
                                            }

//                                        if (exception is FirebaseAuthInvalidUserException) {
//                                            val errorCode = exception.errorCode
//                                            if (errorCode == "ERROR_USER_NOT_FOUND") {
//                                                errorMessage = "Incorrect email or password"
//                                            } else {
//                                                errorMessage = "Login failed: An error occurred."
//                                            }
//                                        } else if (exception is FirebaseAuthInvalidCredentialsException) {
//                                            errorMessage = "Incorrect email or password."

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
                    Text(
                        text = "Or login with:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Card(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    showOneTapUI = true // Set the flag to show the one-tap UI
                                    val activity = context as MainActivity
                                    displayOneTapUI(activity)
//                                    coroutineScope.launch {
//                                        try {
//                                            val signInIntent = googleSignInClient.signInIntent
//                                            val accountTask =
//                                                GoogleSignIn.getSignedInAccountFromIntent(
//                                                    signInIntent
//                                                )
//                                            val account =
//                                                accountTask.getResult(ApiException::class.java)
//
//                                            if (account != null) {
//                                                val authCredential =
//                                                    GoogleAuthProvider.getCredential(
//                                                        account.idToken,
//                                                        null
//                                                    )
//                                                val result = FirebaseAuth
//                                                    .getInstance()
//                                                    .signInWithCredential(authCredential)
//                                                    .await()
//
//                                                if (result.user != null) {
//                                                    // Google registration successful, navigate to the desired screen
//                                                    navController.navigate("patienthome")
//                                                } else {
//                                                    // Google registration failed
//                                                    errorMessage =
//                                                        "Google sign-in: Failed to get Google account."
//                                                    Log.e(
//                                                        "GoogleSignIn",
//                                                        "Failed to get Google account."
//                                                    )
//                                                }
//                                            } else {
//                                                // Handle failure to get account
//                                                errorMessage =
//                                                    "Google sign-in failed: Failed to get Google account."
//                                                Log.e(
//                                                    "GoogleSignIn",
//                                                    "Failed to get Google account."
//                                                )
//                                            }
//                                        } catch (e: ApiException) {
//                                            // Handle the ApiException, e.g., show an error message
//                                            errorMessage =
//                                                "Google sign-in failed: An ApiException occurred."
//                                            Log.e(
//                                                "GoogleSignIn",
//                                                "Google registration failed: ${e.message}"
//                                            )
//                                        } catch (e: Exception) {
//                                            // Handle other general exceptions
//                                            errorMessage = e.message
//                                                ?: "Google sign-in failed: An error occurred. B"
//                                            Log.e(
//                                                "FirebaseAuth",
//                                                "Google registration error: ${e.message}",
//                                                e
//                                            )
//                                        }
//                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            shape = RectangleShape
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = "Google Logo",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        Card(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    //handleGoogleRegistration()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            shape = RectangleShape
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.microsoft_logo),
                                contentDescription = "Microsoft Logo",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onBackground,//Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Don't have an account?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                        TextButton(onClick = {
                            navController.navigate("register")
                        }) {
                            Text(
                                text ="Register Here",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary) //colorResource(id = R.color.my_primary))
                        }
                    }
                }
            }
        }
    }
}



sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOperations: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Email: com.example.gohealth.InputType(
        label = "Email Address",
        icon = Icons.Default.Email,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )
    object Password: com.example.gohealth.InputType(
        label = "Password",
        icon = Icons.Default.Lock,
        keyboardOperations = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    inputType: com.example.gohealth.InputType,
    focusRequester: FocusRequester? = null,
    keyboardActions: KeyboardActions
) {

    var text:String by remember { mutableStateOf("")}

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
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

//@Composable
//fun AppPreview() {
//    GoHealthTheme {
//        Nav()
//    }
//}
