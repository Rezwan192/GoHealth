package com.example.gohealth.patient

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.components.RescheduleAppointment
import com.example.gohealth.data.Appointment
import com.example.gohealth.data.AppointmentRepository
import com.example.gohealth.data.Doctor
import com.example.gohealth.data.DoctorRepository
import com.example.gohealth.data.PatientRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAppointments(navController: NavHostController){
    val appointmentRepository = AppointmentRepository()

    appointmentRepository.updateElapsedAppointmentsToCompleted()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Upcoming Appointments", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Appointments(paddingValues = innerPadding)
    }
}

@Composable
fun Appointments(
    paddingValues: PaddingValues,
    appointmentRepository: AppointmentRepository = AppointmentRepository(),
    doctorRepository: DoctorRepository = DoctorRepository(),
    patientRepository: PatientRepository = PatientRepository()
) {
    val doctors = remember { mutableStateOf<List<Doctor>?>(null) }
    val appointments = remember { mutableStateOf<List<Appointment>?>(null) }
    // State for loading indicator
    val isLoading = remember { mutableStateOf(true) }
    // State for errors
    val error = remember { mutableStateOf<Exception?>(null) }
    // State to refresh screen
    val refreshTrigger = remember { mutableStateOf(false) }
    // Get current logged in userId (patientId)
    val patientId = FirebaseAuth.getInstance().currentUser?.uid

    // Refresh the screen after an appointment is cancelled
    val onAppointmentUpdate = {
        refreshTrigger.value = !refreshTrigger.value
    }

    // Fetch patient, doctor and appointment data
    LaunchedEffect(refreshTrigger.value) {
        isLoading.value = true
        withContext(Dispatchers.Main) {

            val doctorJob = async {
                doctorRepository.getAllDoctors(
                    onSuccess = { fetchedDoctors ->
                        doctors.value = fetchedDoctors
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }

            val appointmentJob = async {
                appointmentRepository.getAppointmentsForPatient(
                    patientId = patientId!!,
                    onSuccess = { fetchedAppointments ->
                        val sortedAppointments = fetchedAppointments.sortedBy { it.time }
                        appointments.value = sortedAppointments
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }

            // Wait for all operations to finish
            doctorJob.await()
            appointmentJob.await()

            isLoading.value = false
        }
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

        appointments.value.isNullOrEmpty() -> {
            // Show "Data is not available" message only if appointments are null or empty
            Text("There are no upcoming appointments")
        }

        else -> {
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                // Create a card for each appointment
                items(appointments.value!!) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        doctors = doctors.value!!,
                        onAppointmentUpdate = onAppointmentUpdate
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    doctors: List<Doctor>,
    onAppointmentUpdate: () -> Unit
) {
    // Get doctor attributes from fetched lists
    val doctorName = doctors.find { it.doctorId == appointment.doctorId }?.let {
        "${it.firstName} ${it.lastName}"
    } ?: "Unknown Doctor"
    val doctorEmail = doctors.find { it.doctorId == appointment.doctorId }?.email
    val doctorPhone = doctors.find { it.doctorId == appointment.doctorId }?.phoneNumber
    val doctorAddress = doctors.find { it.doctorId == appointment.doctorId }?.address
    var showMenu by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showRescheduleAppointmentDialog by remember { mutableStateOf(false) }
    val appointmentRepository = AppointmentRepository()

    // Handle the cancel appointment confirmation dialog
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Cancel Appointment") },
            text = { Text("Are you sure you want to cancel this appointment?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Call the cancelAppointment function
                        appointmentRepository.cancelAppointment(appointment.documentId)
                        onAppointmentUpdate()
                        showConfirmationDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }

    // handle the Reschedule appointment dialog
    if (showRescheduleAppointmentDialog) {
        RescheduleAppointment(
            appointmentId = appointment.documentId,
            onDismiss = { showRescheduleAppointmentDialog = false },
            onAppointmentReschedule = {
                showRescheduleAppointmentDialog = false
                onAppointmentUpdate()
            })
    }

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
            IconButton(
                onClick = { showMenu = !showMenu },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Appointment Options")

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Reschedule Appointment") },
                        onClick = {
                            // Set the flag to show the reschedule dialog
                            showRescheduleAppointmentDialog = true
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Cancel Appointment") },
                        onClick = {
                            // Set the flag to show the confirmation dialog
                            showConfirmationDialog = true
                            showMenu = false
                        }
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(
                        text = "Doctor Name: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = doctorName,
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
                        text = doctorPhone!!,
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
                        text = doctorEmail!!,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row {
                    Text(
                        text = "Address: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    // Format the timestamp to a readable date/time
                    Text(
                        text = doctorAddress!!,
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
                        text = "Status: ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    // Format the timestamp to a readable date/time
                    Text(
                        text = appointment.status,
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
            }
        }
    }
}

@Composable
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
