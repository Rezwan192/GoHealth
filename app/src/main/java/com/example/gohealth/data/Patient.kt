package com.example.gohealth.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Patient(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val gender: String,
    val birthDate: String,
    val ssn: String?,
    val allergies: String?,
    val insurance: String?
)

val patientRepository = PatientRepository()

// manual entering Patient records for testing purposes
fun AddPatientManually(){
    val patient = Patient(
        "Orsa Lambertini",
        "olambertini0@hubpages.com",
        "149-964-1089",
        "7184 Morningstar Circle",
        "Female",
        "6/17/2023",
        null,
        null,
        null
    )
    patientRepository.addPatient(patient)
}

class PatientRepository {
    private val db = Firebase.firestore

    fun addPatient(patient: Patient) {
        db.collection("patients")
            .add(patient)
            .addOnSuccessListener { documentReference ->
                println("Patient added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding patient: $e")
            }
    }
}

