package com.example.gohealth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gohealth.doctor.DoctorHome
import com.example.gohealth.doctor.DoctorLogin
import com.example.gohealth.doctor.DoctorProfile
import com.example.gohealth.doctor.PatientsList
import com.example.gohealth.patient.PatientHome
import com.example.gohealth.patient.PatientProfile


@Composable
fun Nav(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "accounttype"){

        composable(route = "accounttype"){
            ChooseAccountType(navController)
        }
        composable(route = "login"){
            Login(navController)
        }
        composable(route = "doctorlogin"){
            DoctorLogin(navController)
        }
        composable(route = "register"){
            Register(navController)
        }
        composable(route = "forgot"){
            ResetPassword(navController)
        }
        composable(route = "success") {
            ScreenTest(navController)
        }
        composable(route = "patienthome") {
            PatientHome(navController)
        }
        composable(route = "doctorhome") {
            DoctorHome(navController)
        }
        composable(route = "patientprofile"){
            PatientProfile(navController)
        }
        composable(route = "doctorprofile"){
            DoctorProfile(navController)
        }
        composable(route = "patientscreen"){
            PatientsList(navController)
        }
    }
}