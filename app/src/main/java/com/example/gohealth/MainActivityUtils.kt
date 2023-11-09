package com.example.gohealth

import android.content.Intent
import com.example.gohealth.MainActivity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult

private const val REQ_ONE_TAP = 2
private lateinit var oneTapClient: SignInClient
private lateinit var signUpRequest: BeginSignInRequest

fun displayOneTapUI(activity: MainActivity) {
    oneTapClient = Identity.getSignInClient(activity)
    signUpRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(activity.getString(R.string.your_web_client_id))
                // Show all accounts on the device.
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()
    oneTapClient.beginSignIn(signUpRequest)
        .addOnSuccessListener(activity) { result ->
            try {
                activity.startIntentSenderForResult(
                    result.pendingIntent.intentSender, REQ_ONE_TAP,
                    null, 0, 0, 0
                )
            } catch (e: IntentSender.SendIntentException) {
                Log.e("GoogleSignIn", "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }
        .addOnFailureListener(activity) { e ->
            // No Google Accounts found. Just continue presenting the signed-out UI.
            Log.d("GoogleSignIn", e.localizedMessage)
        }
}

