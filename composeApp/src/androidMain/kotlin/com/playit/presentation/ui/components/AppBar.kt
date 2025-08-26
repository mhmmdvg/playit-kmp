package com.playit.presentation.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: (() -> Unit)? = null,
    scrollOffset: Int = 0,
    maxOffset: Int = 200,
) {
    val collapseProgress = min(1f, max(0f, scrollOffset.toFloat() / maxOffset))
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val smallTitleAlpha by animateFloatAsState(
        targetValue = when {
            collapseProgress < 0.4f -> 0f
            collapseProgress < 0.8f -> (collapseProgress - 0.4f) / 0.4f * 0.6f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "smallTitleAlpha"
    )

    TopAppBar(
        modifier = modifier
            .height(64.dp)
            .alpha(smallTitleAlpha)
            .background(Color.White)
            .then(if (collapseProgress > 0.8f) Modifier.drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            } else Modifier)
            .zIndex(1f),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.3).sp
                )
            }
        },
        navigationIcon = {
            onBackPressed?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier
                        .size(44.dp)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Lucide.ArrowLeft,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(smallTitleAlpha * 0.8f + 0.2f),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        )
    )
}