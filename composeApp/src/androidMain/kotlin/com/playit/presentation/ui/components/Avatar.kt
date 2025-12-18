package com.playit.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.playit.data.remote.resources.Resource
import com.playit.viewmodels.CurrentMeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    currentMeVm: CurrentMeViewModel = koinViewModel()
) {
    val currentMe by currentMeVm.currentMe.collectAsState()

    when (currentMe) {
        is Resource.Loading -> {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }

        is Resource.Success -> {
            CacheImage(
                modifier = modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick
                    ),
                imageUrl = currentMe.data?.images?.get(0)?.url ?: ""
            )
        }

        is Resource.Error -> {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
            )
        }
    }
}