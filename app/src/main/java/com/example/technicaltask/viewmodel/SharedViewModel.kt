package com.example.technicaltask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private val _sharedUsername = MutableLiveData<String>()
    val sharedUsername: LiveData<String> = _sharedUsername

    fun setSharedUser(user: String) {
        _sharedUsername.value = user
    }
}