package com.example.gohealth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Doctor(
    val userId: String = "",
    val firstName: String,
    val lastName: String,
    val specialty: String
) {
    constructor(): this("","","","")
}

class DoctorRepository {
    private val db = Firebase.firestore
    private val doctorsCollection = FirebaseFirestore.getInstance().collection("doctors")

    fun getAllDoctors(onSuccess: (List<Doctor>) -> Unit, onFailure: (Exception) -> Unit) {
        doctorsCollection.get()
            // get all non-empty doctor records & convert to Doctor data class
            .addOnSuccessListener { querySnapshot ->
                val doctors = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Doctor::class.java)
                }
                onSuccess(doctors)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}