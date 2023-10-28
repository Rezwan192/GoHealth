package com.example.gohealth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Patient(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val email: String
) {
    // No-argument constructor as required by Firestore
    constructor() : this ("", "","","")
}

class PatientRepository {
    private val db = Firebase.firestore
    private val patientsCollection = FirebaseFirestore.getInstance().collection("patients")

    // add new patient to the Patients collection
    fun addPatient(patient: Patient) {
        db.collection("patients")
            .add(patient)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                println("Patient added with ID: $documentId")
                documentReference.update("id", documentId) // add the document Id to the patient id field
            }
            .addOnFailureListener { e ->
                println("Error adding patient: $e")
            }
    }

    fun getAllPatients(onSuccess: (List<Patient>) -> Unit, onFailure: (Exception) -> Unit) {
        patientsCollection.get()
            // get all non-empty patient records & convert to Patient data class
            .addOnSuccessListener { querySnapshot ->
                val patients = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Patient::class.java)
                }
                onSuccess(patients)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}

