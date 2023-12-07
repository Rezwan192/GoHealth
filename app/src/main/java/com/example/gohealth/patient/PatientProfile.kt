package com.example.gohealth.patient

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.gohealth.R
import com.example.gohealth.data.Patient
import com.example.gohealth.data.PatientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfile(navController: NavHostController, patientId: String) {
    val patientRepository = PatientRepository()

    // State
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Fetch the Patient data using the Patient Id
    fun fetchPatientData(patientId: String) {
        isLoading = true
        patientRepository.getPatient(patientId,
            onSuccess = { fetchedPatient ->
                patient = fetchedPatient
                isLoading = false
            },
            onFailure = { error ->
                isError = true
                isLoading = false
                Log.e("PatientDataFetch", "Error fetching patient data: ${error.localizedMessage} ${patientId}")
            }
        )
    }

    // Handle the opening of the edit profile Dialog
    if (showEditDialog) {
        patient?.let {
            EditPatientDialog(
                patient = it,
                onDismiss = { showEditDialog = false },
                onSave = { updatedFirstName, updatedLastName, updatedEmail, updatedDateOfBirth, updatedAddress,
                           updatedPhoneNumber, updatedHeight, updatedWeight, updatedBloodType, updatedAllergies, updatedInsurance ->
                    val updates = mutableMapOf<String, Any>()

                    // Check for changes in Patient data
                    // if there are changes, add them to the updates collection
                    if (patient!!.firstName != updatedFirstName) {
                        updates["firstName"] = updatedFirstName
                    }
                    if (patient!!.lastName != updatedLastName) {
                        updates["lastName"] = updatedLastName
                    }
                    if (patient!!.email != updatedEmail) {
                        updates["email"] = updatedEmail
                    }

                    if (patient!!.dateOfBirth != updatedDateOfBirth) {
                        updates["dateOfBirth"] = updatedDateOfBirth
                    }
                    if (patient!!.address != updatedAddress) {
                        updates["address"] = updatedAddress
                    }
                    if (patient!!.phoneNumber != updatedPhoneNumber) {
                        updates["phoneNumber"] = updatedPhoneNumber
                    }
                    if (patient!!.height != updatedHeight) {
                        updates["height"] = updatedHeight
                    }
                    if (patient!!.weight != updatedWeight) {
                        updates["weight"] = updatedWeight
                    }
                    if (patient!!.bloodType != updatedBloodType) {
                        updates["bloodType"] = updatedBloodType
                    }
                    if (patient!!.allergies != updatedAllergies) {
                        updates["allergies"] = updatedAllergies
                    }
                    if (patient!!.insurance != updatedInsurance) {
                        updates["insurance"] = updatedInsurance
                    }

                    updatePatientProfile(
                        patientDocumentId = patient!!.documentId,
                        updates = updates,
                        patientRepository,
                        scope = scope,
                        snackbarHostState = snackbarHostState,
                        onSuccess = { showEditDialog = false },
                        onPostUpdate = { fetchPatientData(patientId) },  // Re-fetch Patient data
                        onFailure = {}
                    )
                }
            )
        }
    }

    // Fetch Patient data when the composable loads
    if (patient == null && !isError) {
        fetchPatientData(patientId)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Patient Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                // Edit Profile button
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Profile"
                        )
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
                patient != null -> PatientProfileContent(patient!!)
            }
        }
    }
}

@Composable
fun PatientProfileContent(patient: Patient) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileHeader(patient)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Each patient attribute as an individual card
            item { PatientAttributeCard("First Name", patient.firstName) }
            item { PatientAttributeCard("Last Name", patient.lastName) }
            item { PatientAttributeCard("Date Of Birth", patient.dateOfBirth) }
            item { PatientAttributeCard("Height", patient.height) }
            item { PatientAttributeCard("Weight", patient.weight) }
            item { PatientAttributeCard("Blood Type", patient.bloodType) }
            item { PatientAttributeCard("Email", patient.email) }
            item { PatientAttributeCard("Phone Number", patient.phoneNumber) }
            item { PatientAttributeCard("Primary Doctor", patient.primaryDoctor) }
            item { PatientAttributeCard("Insurance", patient.insurance) }
            item { PatientAttributeCard("Allergies", patient.allergies) }
            item { PatientAttributeCard("Prescriptions", patient.prescriptions) }
        }
    }
}

@Composable
fun ProfileHeader(patient: Patient) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_profile),
            contentDescription = "Profile Picture",
            alignment = Alignment.Center,
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
        )
        Text(
            text = "${patient.firstName} ${patient.lastName}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
fun PatientAttributeCard(label: String, value: String?) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, String, String, String, String) -> Unit
) {
    val scrollState = rememberScrollState()
    var firstName by remember { mutableStateOf(patient.firstName) }
    var lastName by remember { mutableStateOf(patient.lastName) }
    var email by remember { mutableStateOf(patient.email) }
    var dateOfBirth by remember { mutableStateOf(patient.dateOfBirth) }
    var address by remember { mutableStateOf(patient.address) }
    var phoneNumber by remember { mutableStateOf(patient.phoneNumber) }
    var height by remember { mutableStateOf(patient.height) }
    var weight by remember { mutableStateOf(patient.weight) }
    var bloodType by remember { mutableStateOf(patient.bloodType) }
    var allergies by remember { mutableStateOf(patient.allergies) }
    var insurance by remember { mutableStateOf(patient.insurance) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Outlined.Close, contentDescription = "Close")
                    }
                    Text(
                        "Edit Profile",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    TextButton(onClick = { onSave(firstName, lastName, email, dateOfBirth!!, address!!, phoneNumber!!, weight!!, height!!, bloodType!!, allergies!!,
                        insurance!!
                    ) }) {
                        Text("Save", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dateOfBirth!!,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = address!!,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phoneNumber!!,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = height!!,
                    onValueChange = { height = it },
                    label = { Text("Height") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = weight!!,
                    onValueChange = { weight = it },
                    label = { Text("Weight") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = bloodType!!,
                    onValueChange = { bloodType = it },
                    label = { Text("Blood Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = allergies!!,
                    onValueChange = { allergies = it },
                    label = { Text("Allergies") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = insurance!!,
                    onValueChange = { insurance = it },
                    label = { Text("Insurance") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun updatePatientProfile(
    patientDocumentId: String,
    updates: Map<String, Any>,
    patientRepository: PatientRepository,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onSuccess: () -> Unit,
    onPostUpdate: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    patientRepository.updatePatient(patientDocumentId, updates)
        .addOnSuccessListener {
            scope.launch {
                snackbarHostState.showSnackbar("Profile updated successfully")
            }
            onPostUpdate()
            onSuccess()
        }
        .addOnFailureListener { exception ->
            scope.launch {
                snackbarHostState.showSnackbar("Failed to update profile")
            }
            onFailure(exception)
        }
}

