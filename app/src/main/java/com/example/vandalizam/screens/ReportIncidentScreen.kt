package com.example.vandalizam.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vandalizam.MainActivity
import com.example.vandalizam.models.Incident
import com.example.vandalizam.viewmodel.IncidentViewModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIncidentScreen(navController: NavController,
                         incidentViewModel: IncidentViewModel = viewModel(), // Preuzimamo IncidentViewModel
                         onSaveClick: (Incident) -> Unit = {} // OnClick može biti opcionalan sa podrazumevanom vrednošću
) {
    // Koristimo podatke iz ViewModel-a
    var title by remember { mutableStateOf(incidentViewModel.title.value) } // Dodali smo 'by remember' za promene
    var description by remember { mutableStateOf(incidentViewModel.description.value) }
    var photoUrl = incidentViewModel.photoUrl.value
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Prijavi Incident", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Naslov") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Unos opisa incidenta
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis incidenta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dodavanje fotografije incidenta
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Dodaj Fotografiju")
        }

        // Prikaz odabrane fotografije
        imageUri?.let { uri ->
            Text("Fotografija dodata: ${uri.path}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Biranje lokacije (funkcionalnost prebačena u novi Composable)
        Button(onClick = {
            val sanitizedTitle = title.replace(" ", "_")
            val sanitizedDescription = description.ifEmpty { "No description provided" }
            val sanitizedPhotoUrl = photoUrl.ifEmpty { "default_photo_url" }
            navController.navigate("maps_screen/$sanitizedTitle/$sanitizedDescription/$sanitizedPhotoUrl") // Navigacija ka biranju lokacije
        }) {
            Text("Izaberi lokaciju")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}