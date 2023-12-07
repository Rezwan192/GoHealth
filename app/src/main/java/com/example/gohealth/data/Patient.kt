package com.example.gohealth.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Patient(
    val documentId: String,
    val patientId: String = "",
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String?,
    val address: String?,
    val phoneNumber: String?,
    val email: String,
    val height: String?,
    val weight: String?,
    val bloodType: String?,
    val primaryDoctor: String?,
    val allergies: String?,
    val prescriptions: String?,
    val insurance: String?
) {
    // No-argument constructor as required by Firestore
    constructor() : this ("","","","","","","","","", "","",null,"","","")

}

class PatientRepository {
    private val db = Firebase.firestore
    private val patientsCollection = FirebaseFirestore.getInstance().collection("patients")

    // add new patient to the Patients collection
    fun addPatient(patient: Patient) {
        val patientDocumentRef = patientsCollection.document()

        // Set the patient data without documentId
        patientDocumentRef.set(patient)
            .addOnSuccessListener {
                // Update the document with its own ID
                val patientId = patientDocumentRef.id
                patientDocumentRef.update("documentId", patientId)
                    .addOnSuccessListener {
                        println("Patient added with ID: $patientId")
                    }
                    .addOnFailureListener { e ->
                        println("Error updating patient with documentId: $e")
                    }
                    .addOnFailureListener { e ->
                        println("Error adding patient: $e")
                    }
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

    // Get patient using authId
    fun getPatient(authId: String, onSuccess: (Patient) -> Unit, onFailure: (Exception) -> Unit) {
        patientsCollection.whereEqualTo("patientId", authId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val patient = querySnapshot.documents.firstOrNull()?.toObject(Patient::class.java)
                if (patient != null) {
                    onSuccess(patient)
                } else {
                    onFailure(Exception("Patient not found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun updatePatient(documentId: String, updates: Map<String, Any>): Task<Void> {
        return db.collection("patients").document(documentId).update(updates)
    }
}

