package com.example.gohealth.doctor

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gohealth.R
import com.example.gohealth.data.Doctor
import com.example.gohealth.data.DoctorRepository
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import com.example.gohealth.patient.PatientContent
import com.example.gohealth.patient.uploadImageToFirebaseStorage
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHome(navController: NavHostController, modifier: Modifier = Modifier) {
    val doctorRepository = DoctorRepository()
    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    // Fetching doctor data
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    if (currentUserId != null && doctor == null) {
        doctorRepository.getDoctor(currentUserId,
            onSuccess = { fetchedDoctor ->
                doctor = fetchedDoctor
                isLoading = false
            },
            onFailure = { error ->
                isError = true
                isLoading = false
                Log.e("PatientDataFetch", "Error fetching patient data: ${error.localizedMessage}")
            }
        )
    }

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
            doctor != null -> {
                // Render the main content
                DoctorContent(doctor!!, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorContent(doctor: Doctor, navController: NavHostController) {
    val doctorId = doctor.doctorId
    val logoutdialog = remember { mutableStateOf(false) }
    if (logoutdialog.value) {
        AlertDialog(
            onDismissRequest = { logoutdialog.value = false },
            confirmButton = {
                Button(onClick = {
                    logoutdialog.value = false; navController.navigate("accounttype")
                })
                {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(onClick = { logoutdialog.value = false })
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

    Scaffold(Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = "Welcome Back, Dr. Shmoe",
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },

                navigationIcon = {
                    IconButton(onClick = { logoutdialog.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        content = { contentPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(contentPadding),
                content = {
                    item (span = { GridItemSpan(2)}) { ProfilePicture() }
                    item {
                        DoctorMenuCard(
                            icon = Icons.Filled.AccountBox,
                            title = "Profile"
                        ) { navController.navigate("doctorprofile/$doctorId") }
                    }
                    item {
                        DoctorMenuCard(
                            icon = Icons.Rounded.Face,
                            title = "Patients"
                        ) { navController.navigate("patientscreen") }
                    }
                    item { DoctorMenuCard(icon = Icons.Filled.Person, title = "Chat") { /*TODO*/ } }
                    item {
                        DoctorMenuCard(
                            icon = Icons.Filled.DateRange,
                            title = "Appointments"
                        ) { navController.navigate("appointment") }
                    }
                    item {
                        DoctorMenuCard(
                            icon = Icons.Outlined.DateRange,
                            title = "Appointment Requests"
                        ) { /*TODO*/ }
                    }
                    item {
                        DoctorMenuCard(
                            icon = Icons.Rounded.Menu,
                            title = "Documents"
                        ) { navController.navigate("document") }
                    }
                },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorMenuCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .size(225.dp)
            .padding(18.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
                    .padding(top = 15.dp)
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 5.dp)
            )
        }
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.patrick),
        contentDescription = null,
        modifier = Modifier
            .size(180.dp)
            .padding(top = 10.dp, bottom = 15.dp)
            .clip(CircleShape)
    )
}
