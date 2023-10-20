package com.example.gohealth.patient

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PatientProfile(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.my_primary)),
    ){

        //Patient information
        Box(modifier = Modifier
            .size(400.dp, 550.dp)
            .background(Color.White)
            .align(Alignment.BottomCenter)
        ){
            Column(modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Name:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Johnny C. Doe",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "DOB:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " 01/01/2000",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Height:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " 5'8",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Weight:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " 160 LB",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Blood Type:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " AB+",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Contact Info:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " CashShmoney@gmail.com",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Primary Doctor:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Dr. Joe Shmoe",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Prescriptions:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Generic Drug A, Generic Drug B, and Generic Drug C",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = "Allergies:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " N/A",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                    )
                }
            }
        }

        //Top of Profile page
        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ){
                TextButton(onClick = { navController.navigate("patienthome")}) {
                    Text(
                        text = "Back",
                        color = colorResource(id = R.color.white),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_profile),
                    contentDescription = "Image",

                    modifier = Modifier
                        .padding(16.dp)
                        .size(125.dp, 125.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Welcome! John Doe",
                    color = colorResource(id = R.color.white),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

