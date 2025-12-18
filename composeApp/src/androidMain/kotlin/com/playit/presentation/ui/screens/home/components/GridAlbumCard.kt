package com.playit.presentation.ui.screens.home.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playit.presentation.ui.components.CacheImage
import com.playit.presentation.ui.components.shimmerEffect

@Composable
fun GridAlbumCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
//    albumData: Item? = null,
    images: String,
    name: String? = null,
    artist: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearOutSlowInEasing
        ),
        label = "scale_animation"
    )

    Box(
        modifier = modifier
            .background(Color.Transparent)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CacheImage(
                imageUrl = images,
                modifier = Modifier.fillMaxSize()
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Blue,
                        spotColor = Color.Blue,
                    )
                    .clip(RoundedCornerShape(16.dp)),
                description = name ?: "",
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                name?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

               artist?.let {
                   Text(
                       text = it,
                       fontSize = 14.sp,
                       fontWeight = FontWeight.Normal,
                       color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                       maxLines = 1,
                       overflow = TextOverflow.Ellipsis
                   )
               }
            }
        }
    }
}

@Composable
fun SkeletonNewAlbumCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Album cover skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        // Text skeletons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Title skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )

            // Artist skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}