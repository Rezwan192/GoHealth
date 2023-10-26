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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrescriptionList(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.my_primary))
    ){
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ){
            Text(
                modifier = Modifier.padding(12.dp),
                text = "Prescriptions",
                color = colorResource(id = R.color.white),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {/*TODO*/},
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.antibiotics),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(12.dp)
                    )

                    Text(
                        text = "Antibiotics",
                        color = colorResource(id = R.color.black),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.painkillers),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )
                    Text(
                        text = "Painkillers",
                        color = colorResource(id = R.color.black),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.inhaler),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )

                    Text(
                        text = "Asthma Inhaler",
                        color = colorResource(id = R.color.black),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.bloodpressuremedications),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )

                    Text(
                        text = "BP Medication",
                        color = colorResource(id = R.color.black),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}