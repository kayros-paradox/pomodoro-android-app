package com.example.pomodoro.ui.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import com.example.pomodoro.R
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.data.WelcomeScreenPager
import com.example.pomodoro.ui.navigation.NavigationDestination

@Immutable
object WelcomeDestination: NavigationDestination {
    override val route = "Welcome"
}

@Composable
fun WelcomeScreen(navigateToTimer: () -> Unit) {
    val alpha = remember { Animatable(initialValue = 0F) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1F,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Scaffold(
        modifier = Modifier.alpha(alpha.value),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars
    ) { paddingValues ->
        Pager(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(id = R.dimen.padding_large))
                .padding(paddingValues),
            pages = WelcomeScreenPager.listOfPages,
            closeScreen = {
                Option.WelcomeScreen.checked = false
                navigateToTimer()
            }
        )
    }
}

