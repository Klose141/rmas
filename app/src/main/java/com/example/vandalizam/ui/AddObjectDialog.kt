package com.example.vandalizam.ui

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng

@Composable
fun AddObjectDialog(selectedLocation: LatLng?, onLocationSelected: (LatLng) -> Unit) {
    var location by remember { mutableStateOf(selectedLocation) }

    Dialog(onDismissRequest = { /* Handle dismiss */ }) {
        // Prikaz forme sa unetim podacima
        Column {
            Text("Izabrana lokacija: ${location?.latitude}, ${location?.longitude}")

            Button(onClick = {
                // Omogućiti korisniku da izabere lokaciju sa mape
                onLocationSelected(location!!)
            }) {
                Text("Izaberi lokaciju")
            }
        }
    }
}