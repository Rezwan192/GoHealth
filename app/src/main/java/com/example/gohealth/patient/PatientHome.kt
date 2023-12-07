package com.example.gohealth.patient

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.gohealth.R
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


@Composable
fun PatientHome(navController: NavHostController) {
    val patientRepository = PatientRepository()

    // States
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // Fetching patient data
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    if (currentUserId != null && patient == null && !isError) {
        patientRepository.getPatient(currentUserId,
            onSuccess = { fetchedPatient ->
                patient = fetchedPatient
                isLoading = false
            },
            onFailure = { error ->
                isError = true
                isLoading = false
                Log.e("PatientDataFetch", "Error fetching patient data: ${error.localizedMessage}")
            }
        )
    }

    // UI
    Box(
        modifier = Modifier.fillMaxSize().background(color = colorResource(id = R.color.my_primary)),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                // Render a loading state
                CircularProgressIndicator(color = Color.White) // or any other loading indicator
            }
            isError -> {
                // Render an error state
                Text(text = "Failed to load data", color = MaterialTheme.colorScheme.error)
            }
            patient != null -> {
                // Render the main content
                PatientContent(patient!!, navController)
            }
        }
    }
}

fun uploadImageToFirebaseStorage(imageUri: Uri?, patient: Patient) {
    val patientRepository = PatientRepository()
    imageUri?.let { uri ->
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("patient_profile_images/${patient.patientId}.jpg")
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val imageUrl = downloadUri.toString()
                val updates = mapOf("profileImage" to imageUrl)

                val task: Task<Void> = patientRepository.updatePatient(patient.documentId, updates)
            }
        }.addOnFailureListener { exception ->

            Log.e("FirebaseStorage", "Image upload failed: ${exception.message}", exception)
        }
    }
}

@Composable
fun PatientContent(patient: Patient, navController: NavHostController) {
    val patientId = patient.patientId
    val logoutdialog = remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d("PhotoPicker", "Selected URI: $it")
            imageUri = it
            uploadImageToFirebaseStorage(it, patient)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    if(patient.profileImage != "" && imageUri == null) {
        imageUri = Uri.parse(patient.profileImage)
    }

    if (logoutdialog.value){
        AlertDialog(
            onDismissRequest = { logoutdialog.value = false },
            confirmButton = {
                Button(onClick = {logoutdialog.value = false; navController.navigate("accounttype")})
                {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(onClick = {logoutdialog.value = false})
                {
                    Text(text = "Cancel")
                }
            },

            title = {
                Text(text = "You are attempting to logout")
            },
            text = {
                Text(text = "Are you sure?")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.my_primary)),
        contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier.align(Alignment.TopStart))
        {
            IconButton(onClick = { logoutdialog.value = true }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "${patient.firstName} ${patient.lastName}",
                color = colorResource(id = R.color.white),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            AsyncImage(
                modifier = Modifier
                    .padding(16.dp)
                    .size(125.dp, 125.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                model = ImageRequest.Builder(LocalContext.current).data(imageUri)
                    .crossfade(enable = true).build(),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
            )
            LaunchedEffect(imageUri) {
                uploadImageToFirebaseStorage(imageUri, patient)
            }

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = {navController.navigate("patientprofile/$patientId")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profilebuttonicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Profile",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { navController.navigate("patient-appointments") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.appointmenticon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Appointments",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = {navController.navigate("prescription")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prescriptionicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Prescriptions",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { navController.navigate("testresult")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.testresultsicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Test Results",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = { navController.navigate("changedoc")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.changedoctoricon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Change Doctor",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { navController.navigate("chatFeature")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chaticon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Chat",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
