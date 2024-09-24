package com.example.vandalizam.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.vandalizam.models.Incident
import com.example.vandalizam.service.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val incidentsList = remember { mutableStateListOf<Incident>() }
    // Kreiramo state za praćenje statusa dozvole
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Proveravamo da li je dozvola odobrena
    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            // Ako nije, tražimo dozvolu
            locationPermissionState.launchPermissionRequest()
        }
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Vandalizam") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("report_incident") // Navigacija ka prijavi incidenta
            }) {
                Icon(Icons.Default.Add, contentDescription = "Prijavi incident")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dugme za pokretanje servisa
            Button(onClick = {
                Log.d("ServiceTest", "Start button clicked") // Log poruka
                val serviceIntent = Intent(context, LocationService::class.java)
                ContextCompat.startForegroundService(context, serviceIntent)
            }) {
                Text("Pokreni Servis")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dugme za zaustavljanje servisa
            Button(onClick = {
                Log.d("ServiceTest", "Stop button clicked") // Log poruka
                val serviceIntent = Intent(context, LocationService::class.java)
                context.stopService(serviceIntent)
            }) {
                Text("Zaustavi Servis")
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text("Dobrodošli u aplikaciju Vandalizam!")
            Text("Kliknite na dugme da prijavite novi incident.")

            // Dugme za otvaranje Leaderboard ekrana
            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Pogledaj Rang Listu")
            }
        }

    }
}