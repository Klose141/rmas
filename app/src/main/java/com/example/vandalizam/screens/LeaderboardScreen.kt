import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.vandalizam.viewmodels.LeaderboardViewModel
import com.example.vandalizam.models.User

@Composable
fun LeaderboardScreen(navController: NavController, viewModel: LeaderboardViewModel = viewModel()) {
    // Ovde koristimo observeAsState za praćenje promena u korisnicima
    val users: List<User> by viewModel.users.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Rang lista", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users) { user ->
                Text("${user.username}: ${user.points} poena")
                Divider()
            }
        }
    }
}
