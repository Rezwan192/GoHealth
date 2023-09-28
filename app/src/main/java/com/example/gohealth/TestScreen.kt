package com.example.gohealth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController

@Composable
fun ScreenTest(navController: NavHostController, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.chad)
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = image ,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )


}

