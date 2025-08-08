package com.example.pomodoro.ui.welcome

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import com.example.pomodoro.R
import com.example.pomodoro.data.Page

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page,
    offsetFraction: Float,
    selected: Boolean
) {
    val animatedTitleOffset by animateIntOffsetAsState(
        targetValue = IntOffset(
            x = when {
                selected -> -(offsetFraction * 150F).toInt()
                else -> (offsetFraction * 150F).toInt()
            },
            y = 0
        ),
        animationSpec = spring(),
        label = "анимация смещения заголовка слайда"
    )

    val animatedDescriptionOffset by animateIntOffsetAsState(
        targetValue = IntOffset(
            x = when {
                selected -> -(offsetFraction * 200F).toInt()
                else -> (offsetFraction * 200F).toInt()
            },
            y = 0
        ),
        animationSpec = spring(),
        label = "анимация смещения описания слайда"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.55F),
            painter = painterResource(id = page.imageId),
            contentDescription = null
        )
        Text(
            modifier = Modifier.offset { animatedTitleOffset },
            text = stringResource(id = page.titleId),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Text(
            modifier = Modifier.offset { animatedDescriptionOffset },
            text = stringResource(id = page.descriptionId),
            color = MaterialTheme.colorScheme.onTertiary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
