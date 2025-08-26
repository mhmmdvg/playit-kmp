package com.playit.presentation.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min

@Composable
@Preview
fun NavigationTitle(
    modifier: Modifier = Modifier,
    title: String,
    scrollOffset: Int = 0,
    maxOffset: Int = 200,
) {

    val collapseProgress = min(1f, max(0f, scrollOffset.toFloat() / maxOffset))

    val titleSize by animateFloatAsState(
        targetValue = if (collapseProgress < 0.5f) 32f else 20f,
        label = "titleSize"
    )

    val largeTitleAlpha by animateFloatAsState(
        targetValue =  1f - (collapseProgress * 2f).coerceIn(0f, 1f),
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
            Text(
                text = title,
                fontSize = titleSize.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = (-0.5).sp
            )
        }
    }
}