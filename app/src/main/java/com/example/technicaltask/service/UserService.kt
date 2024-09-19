package com.example.technicaltask.service

import com.example.technicaltask.model.User

interface UserService {
    suspend fun getAllUsers(): List<User?>?
    suspend fun getUserByUsername(username: String): User?

    companion object {
        fun create(): UserService {
            return UserServiceImpl()
        }
    }
}