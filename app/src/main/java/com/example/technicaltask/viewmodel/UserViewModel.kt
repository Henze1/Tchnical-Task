package com.example.technicaltask.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technicaltask.model.User
import com.example.technicaltask.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserState {
    data object Loading: UserState()
    data class SuccessAllUsers(val users: List<User?>): UserState()
    data class SuccessUser(val user: User?): UserState()
    data class Error(val message: String): UserState()
}

class UserViewModel: ViewModel() {
    private val userService = UserService.create()
    private val _userState = MutableStateFlow<UserState>(UserState.Loading)
    val userState: StateFlow<UserState> = _userState

    fun fetchAllUsers() {
        _userState.value = UserState.Loading

        viewModelScope.launch {
            try {
                val users = userService.getAllUsers()
                if (users.isNullOrEmpty()) {
                    _userState.value = UserState.Error("No users found")
                } else {
                    _userState.value = UserState.SuccessAllUsers(users)
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Unknown error")
                Log.e("UserViewModel", "Error fetching users: ${e.message}")
            }
        }
    }

    fun fetchUserByUsername(username: String) {
        _userState.value = UserState.Loading

        viewModelScope.launch {
            try {
                val user = userService.getUserByUsername(username)
                if (user == null) {
                    _userState.value = UserState.Error("User not found")
                } else {
                    _userState.value = UserState.SuccessUser(user)
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Unknown error")
                Log.e("UserViewModel", "Error fetching user: ${e.message}")
            }
        }
    }
}