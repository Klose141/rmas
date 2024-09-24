package com.example.vandalizam.navigation

import LeaderboardScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vandalizam.screens.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Odjava korisnika svaki put kada se aplikacija pokrene
    LaunchedEffect(Unit) {
        auth.signOut()
        navController.navigate("login") {
            popUpTo(0) { inclusive = true } // Očisti navigacioni stek
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("home") { HomeScreen(navController) }
        composable("leaderboard") { LeaderboardScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("report_incident") { ReportIncidentScreen(navController) }
        composable("maps_screen/{title}/{description}/{photoUrl}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val description = backStackEntry.arguments?.getString("description")
            val photoUrl = backStackEntry.arguments?.getString("photoUrl")
            MapsScreen(navController, title ?: "", description ?: "", photoUrl ?: "")
        }

    }
}