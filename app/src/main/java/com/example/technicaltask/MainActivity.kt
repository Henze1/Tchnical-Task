package com.example.technicaltask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.technicaltask.navigation.NavigationController
import com.example.technicaltask.ui.theme.TechnicalTaskTheme
import com.example.technicaltask.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userViewModel: UserViewModel by viewModels()

        setContent {
            TechnicalTaskTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                NavigationController(
                    viewModel = userViewModel
                )
            }
        }
    }
}