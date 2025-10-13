package com.playit.presentation.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.presentation.ui.components.stateWrapper
import com.playit.presentation.ui.screens.library.EmptyPlaylistState
import com.playit.presentation.ui.screens.library.components.PlaylistCard
import com.playit.presentation.ui.screens.library.components.SkeletonPlaylistCard
import com.playit.presentation.ui.screens.profile.components.ProfileSection
import com.playit.viewmodels.CurrentPlaylistsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun ProfileScreen(
    modifier: Modifier = Modifier,
    currentPlaylistVm: CurrentPlaylistsViewModel = koinInject()
) {
    val currentPlaylist by remember { currentPlaylistVm.currentPlaylists }.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(20.dp),
    ) {
        item {
            ProfileSection(
                modifier = Modifier.padding(top = 44.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = "Playlists",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

//        items(8) {
//            SkeletonPlaylistCard(
//                modifier = Modifier.fillMaxWidth(),
//            )
//        }

        stateWrapper(
            resource = currentPlaylist,
            onLoading = {
                items(8) {
                    SkeletonPlaylistCard(
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
            onFailure = {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error loading playlists",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentPlaylist.message ?: "Unknown error occurred",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            onSuccess = { data ->
                val playlists = data?.items ?: emptyList()
                if (playlists.isEmpty()) {
                    item {
                        EmptyPlaylistState(
                            onAddPlaylist = { Log.d("ProfileScreen", "Add Playlist") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 60.dp)
                        )
                    }
                } else {
                    items(
                        items = playlists,
                        key = { it.id }
                    ) {
                        PlaylistCard(
                            data = it
                        ) {
                            Log.d("ProfileScreen", "Play Playlist: ${it.name}")
                        }
                    }
                }
            }
        )
    }
}