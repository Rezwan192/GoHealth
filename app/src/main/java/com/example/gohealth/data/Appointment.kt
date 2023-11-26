package com.example.gohealth.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Appointment (
    val documentId: String = "",
    val patientId: String,
    val doctorId: String,
    val time: Long,  // Time of the appointment
    val reason: String,
    val status: String = "Scheduled"  // e.g. Scheduled, Completed, Cancelled
) {
    constructor(): this ("","","",0,"","")
}

class AppointmentRepository {
    private val db = Firebase.firestore
    private val appointmentsCollection = db.collection("appointments")

    fun createAppointment(appointment: Appointment) {
        appointmentsCollection.add(appointment)
            .addOnSuccessListener { documentReference ->
                // Now that the document is created, update it with its generated ID
                val appointmentId = documentReference.id
                appointmentsCollection.document(appointmentId).update("documentId", appointmentId)
                    .addOnSuccessListener {
                        println("Appointment added with ID: $appointmentId")
                    }
                    .addOnFailureListener { e ->
                        println("Error updating appointment with ID: $e")
                    }
            }
            .addOnFailureListener { e ->
                println("Error adding appointment: $e")
            }
    }

    fun cancelAppointment(appointmentId: String) {
        val updateData = mapOf("status" to "Cancelled")
        appointmentsCollection.document(appointmentId).update(updateData)
            .addOnSuccessListener {
                println("Appointment with ID $appointmentId successfully cancelled!")
            }
            .addOnFailureListener { e ->
                println("Error cancelling appointment: $e")
            }
    }

    fun rescheduleAppointment(appointmentId: String, newTimestamp: Long) {
        appointmentsCollection.document(appointmentId)
            .update("timestamp", newTimestamp)
            .addOnSuccessListener {
                println("Appointment successfully rescheduled!")
            }
            .addOnFailureListener { e ->
                println("Error rescheduling appointment: $e")
            }
    }

    fun getAllAppointments(onSuccess: (List<Appointment>) -> Unit, onFailure: (Exception) -> Unit) {
        appointmentsCollection.get()
            // get all non-empty appointment records & convert to Doctor data class
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Appointment::class.java)
                }
                onSuccess(appointments)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getAllScheduledAppointments(onSuccess: (List<Appointment>) -> Unit, onFailure: (Exception) -> Unit) {
        appointmentsCollection.whereEqualTo("status", "Scheduled")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val appointments = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Appointment::class.java)
                }
                onSuccess(appointments)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun updateElapsedAppointmentsToCompleted() {
        val currentTime = System.currentTimeMillis()

        // Query for Scheduled appointments where time is less than the current time
        appointmentsCollection
            .whereEqualTo("status", "Scheduled")
            .whereLessThan("time", currentTime)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val appointmentId = document.id
                    // Update the status to Completed
                    appointmentsCollection.document(appointmentId).update("status", "Completed")
                        .addOnSuccessListener {
                            println("Appointment $appointmentId updated to Completed")
                        }
                        .addOnFailureListener { e ->
                            println("Error updating appointment $appointmentId: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error fetching elapsed appointments: $e")
            }
    }
}