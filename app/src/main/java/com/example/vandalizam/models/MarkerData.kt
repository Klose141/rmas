package com.example.vandalizam.models

import com.google.android.gms.maps.model.LatLng

data class MarkerData(
    val position: LatLng,
    val title: String
)