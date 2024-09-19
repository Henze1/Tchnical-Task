package com.example.technicaltask.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.technicaltask.view.UserListScreen
import com.example.technicaltask.view.UserProfileScreen
import com.example.technicaltask.viewmodel.SharedViewModel
import com.example.technicaltask.viewmodel.UserViewModel

@Composable
fun NavigationController(
    viewModel: UserViewModel,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        modifier = Modifier.
            fillMaxSize(),
        navController = navController,
        startDestination = AllDestinations.USER_LIST
    ) {
        composable(AllDestinations.USER_LIST) {
            UserListScreen(
                viewModel = viewModel,
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(AllDestinations.USER_PROFILE) {
            UserProfileScreen(
                viewModel = viewModel,
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
    }
}