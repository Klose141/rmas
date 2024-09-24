package com.example.vandalizam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.vandalizam.models.Incident
import com.example.vandalizam.services.UserService

class IncidentViewModel : ViewModel() {

    // Mutable state variables for the incident details
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var latitude = mutableStateOf(0.0)
    var longitude = mutableStateOf(0.0)
    var photoUrl = mutableStateOf("")

    // Function to save the incident
    fun saveIncident(userId: String): Incident {
        return Incident(
            title = title.value,
            description = description.value,
            latitude = latitude.value,
            longitude = longitude.value,
            photoUrl = photoUrl.value
        )
        markIncident(userId)
    }

    // Funkcija za dodelu poena korisniku
    private fun markIncident(userId: String) {
        val pointsForIncident = 10 // Dodeli 10 poena po incidentu
        UserService.updateUserPoints(userId, pointsForIncident) {
            // Opcionalno: dodaj obaveštenje ili log o uspešnom dodeljivanju poena
            println("Korisniku $userId dodeljeno $pointsForIncident poena")
        }
    }

    // Optionally, you can add a function to reset the data after saving
    fun resetIncident() {
        title.value = ""
        description.value = ""
        latitude.value = 0.0
        longitude.value = 0.0
        photoUrl.value = ""
    }
}
