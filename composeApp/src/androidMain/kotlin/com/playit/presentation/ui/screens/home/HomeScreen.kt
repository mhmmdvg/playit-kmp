package com.playit.presentation.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.playit.presentation.ui.components.StateWrapper
import com.playit.presentation.ui.screens.home.components.GridAlbumCard
import com.playit.presentation.ui.screens.home.components.SkeletonNewAlbumCard
import com.playit.presentation.ui.screens.home.components.SkeletonSongCard
import com.playit.presentation.ui.screens.home.components.SongCard
import com.playit.viewmodels.CurrentPlaylistsViewModel
import com.playit.viewmodels.NewReleasesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(
    newReleaseVm: NewReleasesViewModel = koinViewModel(),
    currentPlaylistVm: CurrentPlaylistsViewModel = koinViewModel(),
    onScrollOffsetChanged: (Int) -> Unit = {},
    navigationTitle: @Composable () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val newRelease by remember { newReleaseVm.newReleases }.collectAsState()
    val currentPlaylists by remember { currentPlaylistVm.currentPlaylists }.collectAsState()
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val cardWidth = (screenWidth - 40.dp - 44.dp) / 3


    LaunchedEffect(scrollState.value) {
            onScrollOffsetChanged(scrollState.value)
    }

    Column(
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
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large title section
        navigationTitle()

        Column(
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Jump back in",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
            )

            StateWrapper(
                resource = currentPlaylists,
                onLoading = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (1..3).forEach { _ ->
                            SkeletonNewAlbumCard(
                                modifier = Modifier.width(cardWidth),
                            )
                        }
                    }
                },
                onFailure = {},
                onSuccess = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (it?.items?.take(3)?.forEach { playlist ->
                            GridAlbumCard(
                                modifier = Modifier.width(cardWidth),
                                onClick = { Log.d("HomeScreen", "New Album") },
                                name = playlist.name,
                                images = playlist.images[0].url,
                            )
                        })
                    }
                }
            )


            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "New Releases",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                )
                TextButton(
                    onClick = { Log.d("HomeScreen", "New Releases") }
                ) {
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            StateWrapper(
                resource = newRelease,
                onLoading = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (1..3).forEach { _ ->
                            SkeletonNewAlbumCard(
                                modifier = Modifier.width(cardWidth),
                            )
                        }
                    }
                },
                onFailure = {},
                onSuccess = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (newRelease.data?.albums?.items?.take(3)?.forEach { album ->
                            GridAlbumCard(
                                modifier = Modifier.width(cardWidth),
                                onClick = { Log.d("HomeScreen", "New Album") },
                                name = album.name,
                                images = album.images[0].url,
                                artist = album.artists[0].name,
                            )
                        })
                    }
                }
            )

//            when(newRelease) {
//                is Resource.Loading -> {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
//                    ) {
//                        (1..3).forEach {
//                            SkeletonNewAlbumCard(
//                                modifier = Modifier.width(cardWidth),
//                            )
//                        }
//                    }
//                }
//                is Resource.Success -> {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
//                    ) {
//                        (newRelease.data?.albums?.items?.take(3)?.forEach { album ->
//                            NewAlbumCard(
//                                modifier = Modifier.width(cardWidth),
//                                albumData = album,
//                                onClick = { Log.d("HomeScreen", "New Album") }
//                            )
//                        })
//                    }
//                }
//                is Resource.Error -> {}
//            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Song List",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                )
                TextButton(
                    onClick = { Log.d("HomeScreen", "Song List") }
                ) {
                    Text(
                        text = "See All",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            StateWrapper(
                resource = newRelease,
                onLoading = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        (1..3).forEach { _ ->
                            SkeletonSongCard(
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                },
                onFailure = {},
                onSuccess = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        newRelease.data?.albums?.items?.takeLast(3)?.forEach { album ->
                            SongCard(
                                modifier = Modifier.fillMaxWidth(),
                                album = album,
                                onClick = { Log.d("HomeScreen", "Song") }
                            )
                        }
                    }
                }
            )

            // Bottom safe area
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}