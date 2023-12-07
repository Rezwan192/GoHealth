package com.example.gohealth.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Doctor(
    val doctorId: String = "",
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String,
    val insurances: String,
    val languages: String,
    val medLicense: String,
    val phoneNumber: String,
    val education: String,
    val specialty: String,
    val profileImage: String?,
) {
    constructor(): this("","","","","","","","","", "","","")
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

    fun getDoctor(authId: String, onSuccess: (Doctor) -> Unit, onFailure: (Exception) -> Unit) {
        doctorsCollection.whereEqualTo("doctorId", authId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val doctor = querySnapshot.documents.firstOrNull()?.toObject(Doctor::class.java)
                if (doctor != null) {
                    onSuccess(doctor)
                } else {
                    onFailure(Exception("Doctor not found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}