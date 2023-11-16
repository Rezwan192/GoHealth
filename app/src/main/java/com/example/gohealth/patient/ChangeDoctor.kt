package com.example.gohealth.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gohealth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeDoctorScreen(navController: NavHostController) {
    val changedialog = remember { mutableStateOf(false) }

    if (changedialog.value){
        AlertDialog(
            onDismissRequest = { changedialog.value = false },
            confirmButton = {
                Button(onClick = {changedialog.value = false; navController.navigate("accounttype")})
                {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(onClick = {changedialog.value = false})
                {
                    Text(text = "Cancel")
                }
            },

            title = {
                Text(text = "You are changing your doctor.")
            },
            text = {
                Text(text = "Are you sure?")
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.my_primary))){
        Column(
            modifier = Modifier
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(painter = painterResource(id = R.drawable.drjoe), contentDescription = null)
                Column(modifier = Modifier.padding(9.dp)) {
                    Text(text = "Dr. Joe Schmoe",
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }
            }

            Card(onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(painter = painterResource(id = R.drawable.drlinda), contentDescription = null)
                Column(modifier = Modifier.padding(9.dp)) {
                    Text(text = "Dr. Linda Wayne",
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

