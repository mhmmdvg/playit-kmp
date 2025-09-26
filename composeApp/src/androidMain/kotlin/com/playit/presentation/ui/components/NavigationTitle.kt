package com.playit.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.playit.data.remote.resources.Resource
import com.playit.viewmodels.CurrentMeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.math.max
import kotlin.math.min

@Composable
@Preview
fun NavigationTitle(
    modifier: Modifier = Modifier,
    title: String,
    scrollOffset: Int = 0,
    maxOffset: Int = 200,
    currentMeVm: CurrentMeViewModel = koinInject()
) {
    val currentMe by remember { currentMeVm.currentMe }.collectAsState()

    val collapseProgress = min(1f, max(0f, scrollOffset.toFloat() / maxOffset))

    val titleSize by animateFloatAsState(
        targetValue = if (collapseProgress < 0.5f) 32f else 20f,
        label = "titleSize"
    )

    val largeTitleAlpha by animateFloatAsState(
        targetValue = 1f - (collapseProgress * 2f).coerceIn(0f, 1f),
        label = "largeTitleAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.White)
            .zIndex(1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp)
                .alpha(largeTitleAlpha)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = titleSize.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.5).sp
                )

                when (currentMe) {
                    is Resource.Loading -> {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                    }

                    is Resource.Success -> {
                        CacheImage(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            imageUrl = currentMe.data?.images?.get(0)?.url ?: ""
                        )
                    }

                    is Resource.Error -> {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}