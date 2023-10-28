package com.example.gohealth.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Appointment (
    val id: String = "",
    val patientId: String,
    val doctorId: String,
    val timestamp: Long,  // Time of the appointment
    val status: String = "Scheduled"  // e.g. Scheduled, Completed, Cancelled
) {
    constructor(): this ("","","",0,"")
}

class AppointmentRepository {
    private val db = Firebase.firestore
    private val appointmentsCollection = db.collection("appointments")

    fun createAppointment(appointment: Appointment) {
        appointmentsCollection.add(appointment)
            .addOnSuccessListener { documentReference ->
                println("Appointment added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding appointment: $e")
            }
    }

    fun deleteAppointment(appointmentId: String) {
        appointmentsCollection.document(appointmentId).delete()
            .addOnSuccessListener {
                println("Appointment successfully deleted!")
            }
            .addOnFailureListener { e ->
                println("Error deleting appointment: $e")
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
}