package com.playit.presentation.ui.screens.library

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.playit.presentation.ui.components.stateWrapper
import com.playit.presentation.ui.screens.library.components.PlaylistCard
import com.playit.presentation.ui.screens.library.components.SkeletonPlaylistCard
import com.playit.viewmodels.CurrentPlaylistsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
@Preview
fun LibraryScreen(
    onScrollOffsetChanged: (Int) -> Unit = {},
    navigationTitle: @Composable () -> Unit = {},
    onAddPlaylist: () -> Unit = {},
    currentPaylistVm: CurrentPlaylistsViewModel = koinViewModel()
) {
    val currentPaylist by remember { currentPaylistVm.currentPlaylists }.collectAsState()
    val lazyListState = rememberLazyListState()
    val totalScrollOffset = remember {
        derivedStateOf {
            val firstVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()
            if (firstVisibleItem != null) {
                lazyListState.firstVisibleItemIndex * firstVisibleItem.size +
                        lazyListState.firstVisibleItemScrollOffset
            } else {
                0
            }
        }
    }

    LaunchedEffect(totalScrollOffset.value) { onScrollOffsetChanged(totalScrollOffset.value) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    )
                )
            )
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            item {
                navigationTitle()
            }

            stateWrapper(
                resource = currentPaylist,
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
                                text = currentPaylist.message ?: "Unknown error occurred",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                },
                onSuccess = {
                    val playlists = currentPaylist.data?.items ?: emptyList()

                    if (playlists.isEmpty()) {
                        item {
                            EmptyPlaylistState(
                                onAddPlaylist = onAddPlaylist,
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
                                data = it,
                            ) {
                                Log.d("LibraryScreen", it.id)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun EmptyPlaylistState(
    onAddPlaylist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Lucide.Plus,
            contentDescription = "No playlists",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Playlists Yet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your first playlist to organize\nyour favorite music",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddPlaylist,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Create Playlist",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}