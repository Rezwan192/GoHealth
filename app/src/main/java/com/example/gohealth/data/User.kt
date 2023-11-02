package com.example.gohealth.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    val uid: String,              // Firebase Authentication UID
    val email: String,            // Email from Firebase Authentication
    val role: UserRole,           // Enum representing the user role (Doctor or Patient)
) {
    // No-argument constructor as required by Firestore
    constructor() : this("", "", UserRole.PATIENT)
}

enum class UserRole {
    DOCTOR, PATIENT
}

class UserRepository {
    private val db = Firebase.firestore
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    // add new patient to the Patients collection
    fun addUser(user: User) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id
                println("User added with ID: $documentId")
            }
            .addOnFailureListener { e ->
                println("Error adding patient: $e")
            }
    }
}