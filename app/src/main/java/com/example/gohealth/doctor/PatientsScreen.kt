package com.example.gohealth.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.components.ScheduleAppointment
import com.example.gohealth.data.AppointmentRepository
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsList(navController: NavHostController,
                 patientRepository: PatientRepository = PatientRepository(),
) {
    // State for patients
    val patients = remember { mutableStateOf<List<Patient>?>(null) }
    // State for loading indicator
    val isLoading = remember { mutableStateOf(true) }
    // State for errors
    val error = remember { mutableStateOf<Exception?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {

        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Patients",
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge
                ) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            navigationIcon = {
                IconButton(onClick = { navController.navigate("doctorhome")}) {  // Nav to go back to  doc home
                    Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                }
            }
        )

        // Fetch patients when Composable is launched
        LaunchedEffect(Unit) {
            patientRepository.getAllPatients(
                onSuccess = { fetchedPatients ->
                    patients.value = fetchedPatients
                    isLoading.value = false
                },
                onFailure = { e ->
                    error.value = e
                    isLoading.value = false
                }
            )
        }

        when {
            isLoading.value -> {
                // Show a loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            error.value != null -> {
                // Show error message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${error.value?.localizedMessage}")
                }
            }

            else -> {
                // Create a Card for each Patient record
                LazyColumn {
                    items(patients.value ?: listOf()) { patient ->
                        PatientCard(patient = patient, navController)
                    }
                }
            }
        }
    }
}


@Composable
fun PatientCard(patient: Patient,
                navController: NavHostController
) {
    val appointmentRepository = AppointmentRepository()
    val patientId = patient.patientId
    val doctorId = "2XKYG2BgcfSfBZ4yLwCoT8luiEK2" // temporary until doctor auth is fully implemented
    var showDialog by remember { mutableStateOf(false) }

    // handle the Schedule appointment dialog
    if (showDialog) {
        ScheduleAppointment(
            doctorId = doctorId,
            patientId = patientId,
            appointmentRepository = appointmentRepository,
            onDismiss = { showDialog = false },
            onAppointmentCreated = { showDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Patient Profile",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)  // Add some spacing between the icon and the texts
            )

            // Patient Details
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${patient.firstName} ${patient.lastName}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(text = patient.email, style = MaterialTheme.typography.labelSmall)
            }
        }
        Divider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("patientprofile/$patientId") }, // Nav to patient profile
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.padding(8.dp)
            )
            {
                Text(
                    text = "View Profile",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier.padding(8.dp)
            )
            {
                Text(
                    text = "Schedule an Appointment",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        }
    }
}