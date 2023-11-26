package com.example.gohealth.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gohealth.data.Appointment
import com.example.gohealth.data.AppointmentRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleAppointment(
    doctorId: String,
    patientId: String,
    appointmentRepository: AppointmentRepository,
    onAppointmentCreated: () -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isDatePickerDialogShown by remember { mutableStateOf(false) }
    var dateString by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.NOON) }
    var isTimePickerDialogShown by remember { mutableStateOf(false) }
    var timeString by remember { mutableStateOf("") }

    // Update dateString when selectedDate changes
    LaunchedEffect(selectedDate) {
        dateString = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }

    // Update timeString when selectedTime changes
    LaunchedEffect(selectedTime) {
        timeString = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    // Handle the date picker dialog
    if (isDatePickerDialogShown) {
        ShowDatePicker(selectedDate, onDateSelected = { date ->
            selectedDate = date
            isDatePickerDialogShown = false
        }, onDismiss = {
            isDatePickerDialogShown = false
        })
    }

// Handle the time picker dialog
    if (isTimePickerDialogShown) {
        ShowTimePicker(selectedTime, onTimeSelected = { time ->
            selectedTime = time
            isTimePickerDialogShown = false
        }, onDismiss = {
            isTimePickerDialogShown = false
        })
    }

    val confirmButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )

    val dismissButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Create Appointment", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = dateString,
                        readOnly = true,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                                dateString = newValue
                                selectedDate = LocalDate.parse(newValue, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                            }
                        },
                        label = { Text("Date (MM/DD/YYYY)", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { isDatePickerDialogShown = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Select Date")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = timeString,
                        readOnly = true,
                        onValueChange = { /* Ignored as the field is read-only */ },
                        label = { Text("Time (HH:MM)", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = { isTimePickerDialogShown = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Select Time")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for Appointment", style = MaterialTheme.typography.labelMedium) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val zonedDateTime = ZonedDateTime.of(selectedDate, selectedTime, ZoneId.systemDefault())
                    val timestamp = zonedDateTime.toInstant().toEpochMilli()
                    val newAppointment = Appointment(
                        patientId = patientId,
                        doctorId = doctorId,
                        time = timestamp,
                        reason = reason
                    )
                    appointmentRepository.createAppointment(newAppointment)
                    onAppointmentCreated()
                },
                colors = confirmButtonColors
            ) {
                Text("Create Appointment", style = MaterialTheme.typography.labelMedium)
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = dismissButtonColors
            ) {
                Text("Cancel", style = MaterialTheme.typography.labelMedium)
            }
        }
    )
}

@Composable
fun ShowDatePicker(currentDate: LocalDate, onDateSelected: (LocalDate) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val year = currentDate.year
    val month = currentDate.monthValue - 1
    val day = currentDate.dayOfMonth

    val dialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            onDateSelected(LocalDate.of(year, monthOfYear + 1, dayOfMonth))
        }, year, month, day
    )

    // Prevent the user from selecting dates before the current day
    dialog.datePicker.minDate = System.currentTimeMillis() - 1000
    dialog.setOnDismissListener { onDismiss() }
    dialog.show()
}

@Composable
fun ShowTimePicker(
    currentTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    val dialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
            // Only allow the user to select appointment hours from 8:00 am to 4:30 pm
            if (selectedTime.isAfter(LocalTime.of(7, 59)) && selectedTime.isBefore(LocalTime.of(16, 31))) {
                onTimeSelected(selectedTime)
            } else {
                // Show a Toast message if the time is outside the allowed range
                Toast.makeText(context, "Please choose a time between 8:00 AM and 4:30 PM.", Toast.LENGTH_LONG).show()
            }
        },
        currentTime.hour,
        currentTime.minute,
        false  // 'false' for 12-hour view
    )

    dialog.setOnDismissListener { onDismiss() }
    dialog.show()
}