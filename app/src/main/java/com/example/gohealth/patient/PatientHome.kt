package com.example.gohealth.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gohealth.R


@Composable
fun PatientHome(navController: NavHostController) {
    val logoutdialog = remember { mutableStateOf(false) }

    if (logoutdialog.value){
        AlertDialog(
            onDismissRequest = { logoutdialog.value = false },
            confirmButton = {
                Button(onClick = {logoutdialog.value = false; navController.navigate("accounttype")})
                {
                    Text(text = "Confirm")
                }
            },

            dismissButton = {
                Button(onClick = {logoutdialog.value = false})
                {
                    Text(text = "Cancel")
                }
            },

            title = {
                Text(text = "You are attempting to logout")
            },
            text = {
                Text(text = "Are you sure?")
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.my_primary)),
        contentAlignment = Alignment.Center
    ) {

        Row(modifier = Modifier.align(Alignment.TopStart))
        {
            IconButton(onClick = { logoutdialog.value = true }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "John Doe",
                color = colorResource(id = R.color.white),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            // Image at the top
            Image(
                painter = painterResource(id = R.drawable.icon_profile),
                contentDescription = "Image",

                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
                    .size(125.dp, 125.dp)
                    .clip(CircleShape)
            )


            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = {navController.navigate("patientprofile")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profilebuttonicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Profile",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.appointmenticon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Appointments",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = {navController.navigate("prescription")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.prescriptionicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Prescriptions",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { navController.navigate("testresult")},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.testresultsicon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Test Results",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.changedoctoricon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Change Doctor",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }


                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .size(width = 135.dp, height = 100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chaticon),
                            contentDescription = null,  // Image is decorative; no content description needed
                            modifier = Modifier.size(40.dp)  // Adjust the size of the image as needed
                        )
                        Text(
                            text = "Chat",
                            color = colorResource(id = R.color.black),
                            fontSize = 12.sp,  // Adjusted font size to fit with image
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }
    }
}