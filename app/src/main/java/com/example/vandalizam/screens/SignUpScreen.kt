package com.example.vandalizam.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vandalizam.viewmodels.SignUpViewModel
import com.example.vandalizam.viewmodels.SingUpUIEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Korisničko ime") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Uspešna registracija
                            val userId = task.result?.user?.uid ?: ""
                            val user = hashMapOf(
                                "id" to userId,
                                "username" to username,
                                "email" to email,
                                "points" to 0, // Početni broj poena
                                "incidentsMarked" to 0 // Početni broj markiranih incidenata
                            )
                            // Dodaj korisnika u Firestore
                            db.collection("users").document(userId).set(user)
                                .addOnSuccessListener {
                                    // Preusmeri na početni ekran
                                    navController.navigate("home") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Greška prilikom čuvanja korisnika: ${e.message}"
                                }
                        } else {
                            // Prikaži grešku
                            try {
                                throw task.exception ?: Exception("Nepoznata greška")
                            } catch (e: FirebaseAuthUserCollisionException) {
                                errorMessage = "Email je već registrovan!"
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Došlo je do greške!"
                            }
                        }
                    }
            } else {
                errorMessage = "Popunite sva polja!"
            }
        }) {
            Text("Registracija")
        }

        // Prikaz greške ako postoji
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}
