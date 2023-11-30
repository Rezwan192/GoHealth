package com.example.gohealth.doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(navController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF12CEAC))){
        Column(
            modifier = Modifier
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp),
                text = "Documents",
                color = colorResource(id = R.color.white),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Card(onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(painter = painterResource(id = R.drawable.document),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth())
                Column(modifier = Modifier.padding(9.dp)) {
                    Text(text = "Patient Blood test",
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }
            }

            Card(onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
                Image(painter = painterResource(id = R.drawable.documents),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth())
                Column(modifier = Modifier.padding(9.dp)) {
                    Text(text = "Patient Document",
                        fontWeight = FontWeight.W500,
                        color = Color.Black
                    )
                }
            }
        }
    }
}