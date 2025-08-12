package com.playit.presentation.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.presentation.viewmodel.AuthenticationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    authenticationViewModel: AuthenticationViewModel = koinViewModel(),
) {
    val authState by authenticationViewModel.authUiState.collectAsState()


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
                        text = "Logged in: ${if (authState.isAuthenticated) "Yes" else "No"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (!authenticationViewModel.getToken().isNullOrEmpty()) {
                        Text(
                            text = "Token: ${authenticationViewModel.getToken()?.take(20)}...",
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
                    authenticationViewModel.signOut()
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