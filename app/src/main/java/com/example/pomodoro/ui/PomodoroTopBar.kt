package com.example.pomodoro.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.pomodoro.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTopBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    onMenuButtonClick: () -> Unit,
    showMenuButton: Boolean = false
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { TopBarTitle(titleRes = titleRes) },
        actions = {
            AnimatedVisibility(visible = showMenuButton) {
                TopBarMenuButton(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.min_touch_size)),
                    iconRes = iconRes,
                    onClick = onMenuButtonClick
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun TopBarTitle(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int
) {
    Text(
        modifier = modifier,
        text = stringResource(id = titleRes).uppercase(),
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.displayMedium
    )
}

@Composable
fun TopBarMenuButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Default)
    var enabled by remember { mutableStateOf(value = true) }

    IconButton(
        onClick = {
            onClick()
            enabled = !enabled
            scope.launch {
                delay(timeMillis = 650)
                enabled = !enabled
            }
        },
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null
        )
    }
}