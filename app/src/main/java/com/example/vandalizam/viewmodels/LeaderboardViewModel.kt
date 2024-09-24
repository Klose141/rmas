package com.example.vandalizam.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandalizam.models.User
import com.example.vandalizam.services.UserService
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> = _users

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        UserService.getLeaderboard { userList ->
            // Filtriraj samo korisnike koji imaju više od 10 poena
            _users.value = userList.filter { it.points > 10 }
        }
    }

}