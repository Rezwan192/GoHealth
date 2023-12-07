package com.example.gohealth.doctor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.data.Appointment
import com.example.gohealth.data.AppointmentRepository
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentRequests(navController: NavHostController) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Appointment Requests", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        RequestedAppointmentsListContent(paddingValues = innerPadding)
    }
}

@Composable
fun RequestedAppointmentsListContent(
    paddingValues: PaddingValues,
    appointmentRepository: AppointmentRepository = AppointmentRepository(),
    patientRepository: PatientRepository = PatientRepository()
) {
    val patients = remember { mutableStateOf<List<Patient>?>(null) }
    val appointments = remember { mutableStateOf<List<Appointment>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<Exception?>(null) }
    val refreshTrigger = remember { mutableStateOf(false) }

    LaunchedEffect(refreshTrigger.value) {
        isLoading.value = true
        coroutineScope {
            val patientJob = async {
                patientRepository.getAllPatients(
                    onSuccess = { fetchedPatients ->
                        patients.value = fetchedPatients
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }

            val appointmentJob = async {
                appointmentRepository.getAllRequestedAppointments(
                    onSuccess = { fetchedAppointments ->
                        appointments.value = fetchedAppointments.sortedBy { it.time }
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }

            patientJob.await()
            appointmentJob.await()
            isLoading.value = false
        }
    }

    when {
        isLoading.value -> CircularProgressIndicator()
        error.value != null -> Text("Error: ${error.value?.localizedMessage}")
        appointments.value.isNullOrEmpty() -> Text("No appointment requests.")
        else -> LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(appointments.value!!) { appointment ->
                AppointmentRequestCard(
                    appointment,
                    patients.value!!,
                    onDeny = {
                        appointmentRepository.denyAppointment(appointment.documentId,
                            onSuccess = { refreshTrigger.value = !refreshTrigger.value },
                            onFailure = { e ->
                                println("Error denying appointment: $e")
                            }
                        )
                    },
                    onAccept = {
                        appointmentRepository.acceptAppointment(appointment.documentId,
                            onSuccess = { refreshTrigger.value = !refreshTrigger.value },
                            onFailure = { e ->
                                println("Error accepting appointment: $e")
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AppointmentRequestCard(
    appointment: Appointment,
    patients: List<Patient>,
    onDeny: () -> Unit,
    onAccept: () -> Unit
) {
    // Get patient attributes from fetched lists
    val patientName = patients.find { it.patientId == appointment.patientId }?.let {
        "${it.firstName} ${it.lastName}"
    } ?: "Unknown Patient"
    val patientEmail = patients.find { it.patientId == appointment.patientId }?.email ?: "Unknown Patient"
    val patientPhone = patients.find { it.patientId == appointment.patientId }?.phoneNumber ?: "Unknown Patient"

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp,),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(
                        text = "Patient Name: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = patientName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row {
                    Text(
                        text = "Phone Number: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = patientPhone,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row {
                    Text(
                        text = "Email: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = patientEmail,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row {
                    Text(
                        text = "Date of Appointment: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    // Format the timestamp to a readable date/time
                    Text(
                        text = formatTimestamp(timestamp = appointment.time),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row {
                    Text(
                        text = "Reason for appointment: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = appointment.reason,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { onDeny() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Deny", style = MaterialTheme.typography.labelSmall)
                    }
                    Button(
                        onClick = { onAccept() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Accept", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
