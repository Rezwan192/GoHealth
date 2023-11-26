package com.example.gohealth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import com.example.gohealth.data.AppointmentRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RescheduleAppointment(
    appointmentId: String,
    onDismiss: () -> Unit,
    onAppointmentReschedule: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isDatePickerDialogShown by remember { mutableStateOf(false) }
    var dateString by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.NOON) }
    var isTimePickerDialogShown by remember { mutableStateOf(false) }
    var timeString by remember { mutableStateOf("") }
    val appointmentRepository = AppointmentRepository()

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
        title = { Text("Reschedule Appointment", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = dateString,
                        readOnly = true,
                        onValueChange = { newValue ->
                            if (newValue.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                                dateString = newValue
                                selectedDate = LocalDate.parse(
                                    newValue,
                                    DateTimeFormatter.ofPattern("MM/dd/yyyy")
                                )
                            }
                        },
                        label = {
                            Text(
                                "Date (MM/DD/YYYY)",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
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
                        label = {
                            Text(
                                "Time (HH:MM)",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
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
                    label = {
                        Text(
                            "Reason for Appointment",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
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
                    appointmentRepository.rescheduleAppointment(appointmentId, timestamp)
                    onAppointmentReschedule()
                },
                colors = confirmButtonColors
            ) {
                Text("Reschedule Appointment", style = MaterialTheme.typography.labelMedium)
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
