package com.example.gohealth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Nav(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){

        composable(route = "login"){
            Login(navController)
        }

        composable(route = "register"){
            Register(navController)
        }
        composable(route = "success") {
            ScreenTest(navController)
        }
        composable(route = "patient") {
            PatientHome(navController)
        }
    }
}