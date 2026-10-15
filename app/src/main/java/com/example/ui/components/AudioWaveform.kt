package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RadioAmber

@Composable
fun AudioWaveform(
    modifier: Modifier = Modifier,
    isAnimating: Boolean = true,
    barColor: Color = RadioAmber,
    height: Dp = 60.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxWidth().height(height)) {
        val barCount = 32
        val width = size.width
        val canvasHeight = size.height
        val barWidth = width / (barCount * 1.8f)
        val spacing = barWidth * 0.8f

        for (i in 0 until barCount) {
            val x = i * (barWidth + spacing) + barWidth / 2
            val multiplier = if (isAnimating) {
                kotlin.math.sin(phase + i * 0.3f).let { (it + 1f) / 2f }.coerceAtLeast(0.15f)
            } else {
                0.15f
            }
            val barH = (canvasHeight * 0.8f * multiplier).coerceAtLeast(4f)
            val yTop = (canvasHeight - barH) / 2f
            val yBottom = yTop + barH

            drawLine(
                color = barColor,
                start = Offset(x, yTop),
                end = Offset(x, yBottom),
                strokeWidth = barWidth,
                cap = StrokeCap.Round
            )
        }
    }
}
