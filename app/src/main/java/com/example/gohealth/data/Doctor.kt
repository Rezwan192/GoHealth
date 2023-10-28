package com.example.gohealth.data

data class Doctor(
    val id: String = "",
    val firstName: String,
    val lastName: String,
    val specialty: String
) {
    constructor(): this("","","","")
}