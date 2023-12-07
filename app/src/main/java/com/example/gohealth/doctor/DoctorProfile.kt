package com.example.gohealth.doctor

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.R
import com.example.gohealth.data.Doctor
import com.example.gohealth.data.DoctorRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfile(navController: NavHostController, doctorId: String) {
    val doctorRepository= DoctorRepository()

    // State
    var doctor by remember { mutableStateOf<Doctor?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch the Doctor data using the Doctor Id
    fun fetchDoctorData(doctorId: String) {
        isLoading = true
        doctorRepository.getDoctor(doctorId,
            onSuccess = { fetchedDoctor ->
                doctor = fetchedDoctor
                isLoading = false
            },
            onFailure = { error ->
                isError = true
                isLoading = false
                Log.e("DoctorDataFetch", "Error fetching doctor data: ${error.localizedMessage} ${doctorId}")
            }
        )
    }

    // Fetch Doctor data when the composable loads
    if (doctor == null && !isError) {
        fetchDoctorData(doctorId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Doctor Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                isError -> Text("Failed to load data")
                doctor != null -> DoctorProfileContent(doctor!!)
            }
        }
    }
}

@Composable
fun DoctorProfileContent(doctor: Doctor) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileHeader(doctor)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Each patient attribute as an individual card
            item { DoctorAttributeCard("First Name", doctor.firstName) }
            item { DoctorAttributeCard("Last Name", doctor.lastName) }
            item { DoctorAttributeCard("Medical License #", doctor.medLicense) }
            item { DoctorAttributeCard("Languages", doctor.languages) }
            item { DoctorAttributeCard("Education", doctor.education) }
            item { DoctorAttributeCard("Specialty", doctor.specialty) }
            item { DoctorAttributeCard("Email", doctor.email) }
            item { DoctorAttributeCard("Phone Number", doctor.phoneNumber) }
            item { DoctorAttributeCard("Insurance", doctor.insurances) }
        }
    }
}

@Composable
fun ProfileHeader(doctor: Doctor) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.patrick),
            contentDescription = "Profile Picture",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
        )
        Text(
            text = "Dr. ${doctor.firstName} ${doctor.lastName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun DoctorAttributeCard(label: String, value: String?) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
