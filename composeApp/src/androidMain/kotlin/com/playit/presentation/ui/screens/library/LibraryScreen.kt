package com.playit.presentation.ui.screens.library

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import com.playit.viewmodels.CurrentPlaylistsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun LibraryScreen(
    onScrollOffsetChanged: (Int) -> Unit = {},
    navigationTitle: @Composable () -> Unit = {},
    currentPaylistVm: CurrentPlaylistsViewModel = koinInject()
) {
    val currentPaylist by remember { currentPaylistVm.currentPlaylists  }.collectAsState()

    Log.d("LibraryScreen", "currentPlaylist: ${currentPaylist.data.toString()}")

    Column {
        Text(text = "Library Screen", fontWeight = FontWeight.Bold)
    }
}