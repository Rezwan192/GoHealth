package com.example.gohealth.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gohealth.R


@Composable
fun ChangeDoctorScreen(navController: NavHostController) {
    val changedialog = remember { mutableStateOf(false) }

    if (changedialog.value){
        AlertDialog(
            onDismissRequest = { changedialog.value = false },
            confirmButton = {
                Button(onClick = {changedialog.value = false})
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
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Change Doctor",
                color = colorResource(id = R.color.white),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Card(modifier = Modifier
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.drjoe), contentDescription = null)
                Column(modifier = Modifier.padding(9.dp)) {
                    Row {
                        Column {
                            Text(text = "Dr. Joe Shmoe",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                            Text(text = "Physician",
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight())

                        Button(
                            onClick = { changedialog.value = true }
                        ) {
                            Text(text = "Change")
                        }
                    }
                }
            }

            Card(modifier = Modifier
                .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.drlinda), contentDescription = null)
                Column(modifier = Modifier.padding(9.dp)) {
                    Row {
                        Column {
                            Text(text = "Dr. Linda Wayne",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                            Text(text = "Cardiologist",
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight())

                        Button(modifier = Modifier,
                            onClick = { changedialog.value = true }
                        ) {
                            Text(text = "Change")
                        }
                    }
                }
            }
        }
    }
}

