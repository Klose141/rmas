package com.example.vandalizam.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.vandalizam.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

object UserService {
    private val db = FirebaseFirestore.getInstance()

    // Funkcija koja ažurira broj poena
    fun updateUserPoints(userId: String, pointsToAdd: Int, onComplete: () -> Unit) {
        val userRef = Firebase.firestore.collection("users").document(userId)

        Firebase.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)

            if (snapshot.exists()) {
                // Ako korisnik postoji, ažuriraj poene
                val currentPoints = snapshot.getLong("points") ?: 0
                transaction.update(userRef, "points", currentPoints + pointsToAdd)
            } else {
                // Ako korisnik ne postoji, kreiraj ga i postavi početne poene
                val newUser = hashMapOf(
                    "id" to userId,
                    "username" to FirebaseAuth.getInstance().currentUser?.displayName.orEmpty(),
                    "points" to pointsToAdd,
                    "incidentsMarked" to 1 // početni broj incidenata
                )
                transaction.set(userRef, newUser)
            }
        }.addOnSuccessListener {
            // Uspešno ažurirano
            onComplete()
        }.addOnFailureListener { e ->
            Log.w("Firebase", "Error updating points", e)
        }
    }

    // Funkcija za preuzimanje korisnika sa rang liste
    fun getLeaderboard(onResult: (List<User>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .orderBy("points", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val usersList = mutableListOf<User>()
                for (document in result) {
                    val user = document.toObject(User::class.java)
                    usersList.add(user)
                }
                onResult(usersList)
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents: ", exception)
                onResult(emptyList()) // Vraćamo praznu listu u slučaju greške
            }
    }
}
