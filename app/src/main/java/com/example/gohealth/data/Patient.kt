package com.example.gohealth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Patient(
    val firstName: String,
    val lastName: String,
    val email: String
)

class PatientRepository {
    private val db = FirebaseFirestore.getInstance()

    // add new patient to the Patients collection
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

