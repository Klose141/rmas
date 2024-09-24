package com.example.vandalizam.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.vandalizam.models.Incident
import com.example.vandalizam.models.MarkerData
import com.example.vandalizam.services.UserService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapsScreen(navController: NavController,
               incidentTitle: String,
               incidentDescription: String,
               incidentPhotoUrl: String
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cameraPositionState = rememberCameraPositionState()
    val nis = LatLng(43.321445, 21.896104)
    val incidentsList = remember { mutableStateListOf<Incident>() }
    var showConfirmButton by remember { mutableStateOf(false) } // Za kontrolu prikaza dugmeta za potvrdu


    // Učitavanje incidenta iz Firebase-a
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("incidents")
            .get()
            .addOnSuccessListener { documents ->
                incidentsList.clear() // Očisti pre dodavanja novih
                for (document in documents) {
                    val incident = document.toObject(Incident::class.java)
                    incidentsList.add(incident)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error loading incidents", e)
            }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            selectedLocation = latLng
            showConfirmButton = true // Prikaži dugme za potvrdu
        }
    ) {
        // Prikazivanje prethodnih markera iz Firebase-a
        incidentsList.forEach { incident ->
            Marker(
                state = MarkerState(position = LatLng(incident.latitude, incident.longitude)),
                title = incident.title,
                snippet = incident.description
            )
        }

        // Prikazivanje markera na osnovu nove odabrane lokacije
        selectedLocation?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Nova lokacija"
            )
        }

    }

    // Prikaz dugmeta za potvrdu kada korisnik odabere lokaciju
    if (showConfirmButton && selectedLocation != null) {
        // Prikaz dugmeta ispod mape
        Button(
            onClick = {
                selectedLocation?.let {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val userId = currentUser?.uid

                    if (userId != null) {
                        // Dodaj incident u Firebase
                        Firebase.firestore.collection("incidents").add(
                            Incident(
                                title = incidentTitle,
                                latitude = it.latitude,
                                longitude = it.longitude,
                                description = incidentDescription,
                                photoUrl = incidentPhotoUrl
                            )
                        ).addOnSuccessListener {
                            // Ažuriraj poene za korisnika
                            UserService.updateUserPoints(userId, 10) {
                                navController.popBackStack()
                            }
                        }
                    } else {
                        Log.e("Firebase", "User is not logged in.")
                    }
                }
            }
        ) {
            Text("Potvrdi marker i vrati se")
        }
    }

        //ovo sigurno radi
        // Postavi kameru na trenutnu lokaciju pri pokretanju
        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                15f
                            )
                        )
                    }
                }
            }
        }
}










