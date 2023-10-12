package com.example.gohealth

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DoctorProfile(){
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
                        text = "Medical License #:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " 12345678",
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
                        text = "Languages:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " English, Spanish and Japanese",
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
                        text = "Education:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Harvard University",
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
                        text = "Specialty:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Family Medicine",
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
                        text = "Experience:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " 8+ Years",
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
                        text = " realdoctor@gmail.com",
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
                        text = "Phone Number:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " (123)-456-7890",
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
                        text = "Insurances:",
                        color = colorResource(id = R.color.black),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = " Medicare, Medicaid, Blue Shield Cross",
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
                TextButton(onClick = { /*TODO*/ }) {
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
                    text = "Welcome! Dr. Joe Shmoe",
                    color = colorResource(id = R.color.white),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
