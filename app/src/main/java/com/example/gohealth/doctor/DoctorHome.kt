package com.example.gohealth.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHome(navController: NavHostController, modifier: Modifier = Modifier) {
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

    Scaffold(Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(text = "Welcome Back, Dr. Shmoe", maxLines = 2, style = MaterialTheme.typography.headlineLarge)
                },

                navigationIcon = {
                    IconButton(onClick = { logoutdialog.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        },
        content = { contentPadding ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(contentPadding),
                content = {
                    item(span = { GridItemSpan(2)}) { ProfilePicture() }
                    item { DoctorMenuCard(icon = Icons.Filled.AccountBox, title = "Profile") { navController.navigate("doctorprofile")} }
                    item { DoctorMenuCard(icon = Icons.Rounded.Face, title = "Patients") { navController.navigate("patientscreen")} }
                    item { DoctorMenuCard(icon = Icons.Filled.Person, title = "Chat") { /*TODO*/} }
                    item { DoctorMenuCard(icon = Icons.Filled.DateRange, title = "Appointments") { navController.navigate("appointment")} }
                    item { DoctorMenuCard(icon = Icons.Outlined.DateRange, title = "Appointment Requests") { /*TODO*/} }
                    item { DoctorMenuCard(icon = Icons.Rounded.Menu, title = "Documents") { navController.navigate("document")} }
                },
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorMenuCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .size(225.dp)
            .padding(18.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
                    .padding(top = 15.dp)
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 5.dp)
            )
        }
    }
}

@Composable
fun ProfilePicture(modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.medium,
    ) {
        Icon(
            imageVector = Icons.Rounded.AccountCircle ,
            contentDescription = null,
            modifier = Modifier
                .size(175.dp)
                .padding(top = 10.dp, bottom = 15.dp)
        )
    }
}