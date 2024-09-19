package com.example.technicaltask.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
fun UserProfileScreen(
    viewModel: UserViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {
    sharedViewModel.sharedUsername.value?.let { viewModel.fetchUserByUsername(it) }
    val usersState = viewModel.userState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Profile",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(AllDestinations.USER_LIST)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = Color.White
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

            is UserState.SuccessUser -> {
                DisplayUserProfile(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    user = (usersState.value as UserState.SuccessUser).user
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
fun DisplayUserProfile(
    modifier: Modifier,
    user: User?
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0XFFECECEC)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    spotColor = Color.DarkGray
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0XFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0XFFF5F5F5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(200.dp),
                    painter = rememberAsyncImagePainter(model = user?.avatarUrl),
                    contentDescription = "User avatar"
                )
                Text(
                    text = user?.name ?: "",
                    style = MaterialTheme.typography.titleLarge
                )

                Row {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "location"
                    )

                    Text(
                        text = user?.location ?: ""
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${user?.followers} Followers",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    VerticalDivider(
                        modifier = Modifier
                            .height(10.dp),
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${user?.following} Following",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0XFFFAFAFA))
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                user?.bio?.let {
                    Text(text = "Bio:")
                    Text(text = it)
                }

                user?.publicRepos?.let {
                    Text(text = "Public repository:")
                    Text(text = it.toString())
                }

                user?.publicGists?.let {
                    Text(text = "Public gists:")
                    Text(text = it.toString())
                }

                user?.updatedAt?.let {
                    Text(text = "Updated at:")
                    Text(text = it)
                }
            }
        }
    }
}