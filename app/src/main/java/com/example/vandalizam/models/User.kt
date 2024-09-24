package com.example.vandalizam.models

data class User(
    val id: String = "",
    val username: String = "",
    var points: Int = 0,
    var incidentsMarked: Int = 0
)
