package com.playit.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.remote.repository.AuthenticationRepository
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    authenticationRepository: AuthenticationRepository = koinInject()
) {
    val isAuthenticated by remember {
        derivedStateOf { authenticationRepository.isUserLoggedIn() }
    }

    val accessToken by remember {
        derivedStateOf { authenticationRepository.getAccessToken() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome text
            Text(
                text = "Welcome to the main app!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Debug info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Debug Info:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Logged in: ${if (isAuthenticated) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (!accessToken.isNullOrEmpty()) {
                        Text(
                            text = "Token: ${accessToken?.take(20)}...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4CAF50) // Green color
                        )
                    } else {
                        Text(
                            text = "Token: Not found",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF44336) // Red color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign out button
            Button(
                onClick = {
                    authenticationRepository.logout()
                    onSignOut()
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336) // Red background
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sign Out",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}