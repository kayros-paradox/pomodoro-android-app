package com.example.pomodoro.ui.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.pomodoro.R
import com.example.pomodoro.data.timer.OperatingMode

@Composable
fun TimerCircle(
    darkTheme: Boolean,
    operatingMode: OperatingMode,
    currentSeconds: Float,
    maxSeconds: Int,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    onClick: () -> Unit
) {
    val seconds = remember { Animatable(initialValue = 0F) }
    val circleAlpha = remember { Animatable(initialValue = 1F) }
    val backgroundArcColor = remember {
        if (darkTheme) colorScheme.onPrimary else colorScheme.tertiary
    }
    val gradientArcColors = remember (darkTheme) {
        if (darkTheme)
            listOf(colorScheme.primary, colorScheme.primary, colorScheme.primary)
        else
            listOf(colorScheme.secondary, colorScheme.primary, colorScheme.onBackground)
    }
    val iconTint = remember (darkTheme) {
        if (darkTheme) colorScheme.onPrimary else colorScheme.onSurface
    }

    LaunchedEffect(operatingMode) {
        circleAlpha.animateTo(
            targetValue = if (operatingMode.isPaused()) 0.8F else 1F,
            animationSpec = tween(durationMillis = 200)
        )
    }

    LaunchedEffect(currentSeconds) {
        seconds.animateTo(
            targetValue = currentSeconds,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 1F),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .alpha(circleAlpha.value)
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            drawArcs(
                progressValue = seconds.value,
                maxValue = maxSeconds.toFloat(),
                gradientColorsList = gradientArcColors,
                backgroundArcColor = backgroundArcColor
            )
        }
        IconButton(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick
        ) {
            AnimatedContent(
                targetState = operatingMode.isActive(),
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 100)
                    ) togetherWith fadeOut(animationSpec = tween(durationMillis = 100))
                },
                label = "анимация кнопки активации таймера"
            ) {
                when {
                    it -> Spacer(modifier = Modifier.fillMaxSize(fraction = 0.35F))
                    else ->
                        Icon(
                            painter = painterResource(id = operatingMode.iconId),
                            contentDescription = stringResource(id = operatingMode.nameId),
                            modifier = Modifier.fillMaxSize(fraction = 0.35F),
                            tint = iconTint
                        )
                }
            }
        }
    }
}

fun DrawScope.drawArcs(
    backgroundArcColor: Color,
    gradientColorsList: List<Color>,
    //thickness: Float = 25F,
    //offsetVal: Float = 80F,
    //internalThickness: Float = 100F,
    //internalOffsetVal: Float = 130F,
    maxValue: Float = 1F,
    progressValue: Float = 0F
) {
    val arcSize = size.width - 160F // 2 * offsetVal

    drawArc(
        color = backgroundArcColor,
        startAngle = 0F,
        sweepAngle = 360F,
        useCenter = true,
        topLeft = Offset(x = 80F, y = 80F),
        size = Size(arcSize, arcSize),
        style = Stroke(
            width = 25F,
            cap = StrokeCap.Round
        )
    )
    drawArc(
        brush = Brush.linearGradient(gradientColorsList),
        startAngle = 0F,
        sweepAngle = 360F * progressValue / maxValue,
        useCenter = false,
        topLeft = Offset(x = 80F, y = 80F),
        size = Size(arcSize, arcSize),
        style = Stroke(
            width = 25F,
            cap = StrokeCap.Round
        )
    )
    drawArc(
        brush = Brush.linearGradient(gradientColorsList),
        startAngle = 0F,
        sweepAngle = 360F * progressValue / maxValue,
        useCenter = true,
        topLeft = Offset(x = 130F, y = 130F),
        size = Size(
            width = arcSize - 100F,
            height = arcSize - 100F
        ),
    )
}
