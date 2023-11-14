package com.example.gohealth.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gohealth.data.Appointment
import com.example.gohealth.data.AppointmentRepository
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppointmentDialog(
    doctorId: String,
    patientId: String,
    appointmentRepository: AppointmentRepository,
    onAppointmentCreated: () -> Unit,
    onDismiss: () -> Unit
) {
    var isDatePickerDialogShown = remember {mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    if (isDatePickerDialogShown.value) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Create Appointment") },
            text = {
                Column {
                    TextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Reason for Appointment") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { isDatePickerDialogShown.value = true }) {
                        Text("Select Date")
                    }
                    // Display selected date
                    if (selectedDate != LocalDate.now()) {
                        Text("Selected date: ${selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val timestamp = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        val newAppointment = Appointment(
                            patientId = patientId,
                            doctorId = doctorId,
                            time = timestamp,
                            reason = reason
                        )
                        appointmentRepository.createAppointment(newAppointment)
                        onAppointmentCreated()
                    }
                ) {
                    Text("Create Appointment")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )

    }
}