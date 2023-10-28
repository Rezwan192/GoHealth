package com.example.gohealth.doctor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.data.Appointment
import com.example.gohealth.data.AppointmentRepository
import com.example.gohealth.data.Doctor
import com.example.gohealth.data.DoctorRepository
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAppointments(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Upcoming Appointments")
                },
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            )
        }
    ) { innerPadding ->
        AppointmentsListContent(paddingValues = innerPadding)
    }
}

@Composable
fun AppointmentsListContent(
    paddingValues: PaddingValues,
    appointmentRepository: AppointmentRepository = AppointmentRepository(),
    doctorRepository: DoctorRepository = DoctorRepository(),
    patientRepository: PatientRepository = PatientRepository()
) {

    val patients = remember { mutableStateOf<List<Patient>?>(null) }
    val doctors = remember { mutableStateOf<List<Doctor>?>(null) }
    val appointments = remember { mutableStateOf<List<Appointment>?>(null) }
    // State for loading indicator
    val isLoading = remember { mutableStateOf(true) }
    // State for errors
    val error = remember { mutableStateOf<Exception?>(null) }

    LaunchedEffect(Unit) {
        // Using a coroutine scope to fetch data in parallel
        coroutineScope {
            launch {
                patientRepository.getAllPatients(
                    onSuccess = { fetchedPatients ->
                        patients.value = fetchedPatients
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }
            launch {
                doctorRepository.getAllDoctors(
                    onSuccess = { fetchedDoctors ->
                        doctors.value = fetchedDoctors
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }
            launch {
                appointmentRepository.getAllAppointments(
                    onSuccess = { fetchedAppointments ->
                        appointments.value = fetchedAppointments
                    },
                    onFailure = { e ->
                        error.value = e
                    }
                )
            }
        }
        // Once all three have been fetched, set isLoading to false
        isLoading.value = false
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
            // Create a Card for each appointment record
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                items(appointments.value!!) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        doctors = doctors.value!!,
                        patients = patients.value!!
                    )
                }
            }
        }
    }


}

@Composable
fun AppointmentCard(appointment: Appointment, doctors: List<Doctor>, patients: List<Patient>) {
    // Get doctor and patient names from fetched lists
    val doctorName = doctors.find { it.id == appointment.doctorId }?.let {
        "${it.firstName} ${it.lastName}"
    } ?: "Unknown Doctor"
    val patientName = patients.find { it.id == appointment.patientId }?.let {
        "${it.firstName} ${it.lastName}"
    } ?: "Unknown Patient"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Appointment ID: ${appointment.id}")
            Text(text = "Patient Name: $patientName")
            Text(text = "Doctor Name: $doctorName")
            Text(text = "Status: ${appointment.status}")
            // Format the timestamp to a readable date/time
            Text(text = "Date: ${formatTimestamp(appointment.timestamp)}")
        }
    }
}

@Composable
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}