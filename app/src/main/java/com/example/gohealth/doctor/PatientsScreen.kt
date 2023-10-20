package com.example.gohealth.doctor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository

@Composable
fun PatientsList(navController: NavHostController, patientRepository: PatientRepository = PatientRepository()) {
    // State for patients
    val patients = remember { mutableStateOf<List<Patient>?>(null) }
    // State for loading indicator
    val isLoading = remember { mutableStateOf(true) }
    // State for errors
    val error = remember { mutableStateOf<Exception?>(null) }

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
                    PatientCard(patient = patient)
                }
            }
        }
    }
}

@Composable
fun PatientCard(patient: Patient) {
    // Create a card for the patient.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Name: ${patient.lastName}")
            Text(text = "Email: ${patient.email}")
        }
    }
}

//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "DefaultPreviewDark"
//)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//    name = "DefaultPreviewLight"
//)
//
//@Composable
//fun AppPreview() {
//    GoHealthTheme {
//        PatientsList()
//    }
//}