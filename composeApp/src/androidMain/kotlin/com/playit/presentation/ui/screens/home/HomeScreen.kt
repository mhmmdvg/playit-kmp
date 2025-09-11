package com.playit.presentation.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.playit.presentation.ui.screens.home.components.NewAlbumCard
import com.playit.presentation.ui.screens.home.components.SkeletonNewAlbumCard
import com.playit.presentation.ui.screens.home.components.SkeletonSongCard
import com.playit.presentation.ui.screens.home.components.SongCard
import com.playit.data.remote.resources.Resource
import com.playit.viewmodels.NewReleasesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(
    newReleaseVm: NewReleasesViewModel = koinInject(),
    onScrollOffsetChanged: (Int) -> Unit = {},
    navigationTitle: @Composable () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val newRelease by remember { newReleaseVm.newReleases }.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            newReleaseVm.onCleared()
        }
    }

    LaunchedEffect(scrollState.value) {
        onScrollOffsetChanged(scrollState.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFAFAFA)
                    )
                )
            )
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large title section
        navigationTitle()

        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Your personalized dashboard",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = TextAlign.Start
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
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

            when(newRelease) {
                is Resource.Loading -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (1..3).forEach {
                            SkeletonNewAlbumCard(
                                modifier = Modifier.weight(0.3f)
                            )
                        }
                    }
                }
                is Resource.Success -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(22.dp, alignment = Alignment.CenterHorizontally),
                    ) {
                        (newRelease.data?.albums?.items?.take(3)?.forEach { album ->
                            NewAlbumCard(
                                modifier = Modifier.weight(0.3f),
                                albumData = album,
                                onClick = { Log.d("HomeScreen", "New Album") }
                            )
                        })
                    }
                }
                is Resource.Error -> {}
            }

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

            when(newRelease) {
                is Resource.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        (1..3).forEach {
                            SkeletonSongCard(
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
                is Resource.Success -> {
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
                is Resource.Error -> {}
            }

            // Bottom safe area
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}