package com.example.technicaltask.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.technicaltask.model.User
import com.example.technicaltask.navigation.AllDestinations
import com.example.technicaltask.viewmodel.SharedViewModel
import com.example.technicaltask.viewmodel.UserState
import com.example.technicaltask.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    viewModel.fetchAllUsers()
    val usersState = viewModel.userState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "GitHub",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    //has nothing to do, just to keep top app bar content equal
                    IconButton(
                        onClick = {},
                        enabled = false
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = Color(0XFF77448E)
                        )
                    }
                },
                actions = {
                    //has nothing to do, just to keep top app bar content equal
                    IconButton(
                        onClick = {},
                        enabled = false
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = Color(0XFF77448E)
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color(0XFF77448E),
                    titleContentColor = Color.White,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { innerPadding ->
        when (usersState.value ) {
            is UserState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is UserState.SuccessAllUsers -> {
                DisplayUsersList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    users = (usersState.value as UserState.SuccessAllUsers).users,
                    sharedViewModel = sharedViewModel,
                    navController = navController
                )
            }

            is UserState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = (usersState.value as UserState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun DisplayUsersList(
    modifier: Modifier,
    users: List<User?>,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = users.size,
            key = { users[it]?.id ?: 0 },
        ) {
            val user = users[it]

            val userId = user?.id ?: return@items

            val username = remember { mutableStateMapOf<Int, String?>() }
            val imageUrl = remember { mutableStateMapOf<Int, String?>() }

            try {
                username[userId] = username[userId] ?: user.login
                imageUrl[userId] = imageUrl[userId] ?: user.avatarUrl
            } catch (e: Exception) {
                Log.e("ListUsers", "${e.message}",  e.cause)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        sharedViewModel.setSharedUser(username[userId].toString())
                        navController.navigate(AllDestinations.USER_PROFILE)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .height(92.dp)
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(
                            model = imageUrl[userId]
                        ),
                        contentDescription = "User avatar",
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text(
                            text = username[userId] ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1
                        )

                        Text(
                            text = "id:${userId}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                IconButton(
                    modifier = Modifier
                        .size(60.dp),
                    onClick = {
                        sharedViewModel.setSharedUser(username[userId].toString())
                        navController.navigate(AllDestinations.USER_PROFILE)
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "to user profile",
                        tint = Color.Gray
                    )
                }
            }

            HorizontalDivider(
                color = Color(0XFFD3D3D3)
            )
        }
    }
}