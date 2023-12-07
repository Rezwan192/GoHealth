package com.example.gohealth

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gohealth.doctor.DoctorAppointments
import com.example.gohealth.doctor.DoctorHome
import com.example.gohealth.doctor.DoctorLogin
import com.example.gohealth.doctor.DoctorProfile
import com.example.gohealth.doctor.DocumentScreen
import com.example.gohealth.doctor.PatientsList
import com.example.gohealth.patient.ChangeDoctorScreen
import com.example.gohealth.patient.PatientHome
import com.example.gohealth.patient.PatientProfile
import com.example.gohealth.patient.PrescriptionList
import com.example.gohealth.patient.TestResultScreen


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
        composable(
            route = "patientprofile/{patientId}",
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the patient ID from the backStackEntry arguments
            val patientId = backStackEntry.arguments?.getString("patientId")
            PatientProfile(navController, patientId!!)
        }
        composable(
            route = "doctorprofile/{doctorId}",
            arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the doctor ID from the backStackEntry arguments
            val doctorId= backStackEntry.arguments?.getString("doctorId")
            DoctorProfile(navController, doctorId!!)
        }
        composable(route = "patientscreen"){
            PatientsList(navController)
        }
        composable(route = "prescription"){
            PrescriptionList(navController)
        }
        composable(route = "appointment"){
            DoctorAppointments(navController)
        }
        composable(route = "testresult"){
            TestResultScreen(navController)
        }
        composable(route = "document"){
            DocumentScreen(navController)
        }
        composable(route = "changedoc"){
            ChangeDoctorScreen(navController)
        }
        composable(route = "chatFeature"){
            ChatFeature(navController)
        }
    }
}