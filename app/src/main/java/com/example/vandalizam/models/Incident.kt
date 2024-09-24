package com.example.vandalizam.models

import android.graphics.Bitmap

data class Incident(
    val title: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val type: String = "",
    val description: String = "",
    val photoUrl: String = ""
)
