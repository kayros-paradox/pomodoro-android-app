package com.example.pomodoro.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.service.PomodoroService.Action
import com.example.pomodoro.service.pomodoroServiceIntent
import com.example.pomodoro.ui.menu.MenuDestination
import com.example.pomodoro.ui.menu.MenuScreen
import com.example.pomodoro.ui.stats.StatsDestination
import com.example.pomodoro.ui.stats.StatsScreen
import com.example.pomodoro.ui.theme.Theme
import com.example.pomodoro.ui.timer.TimerDestination
import com.example.pomodoro.ui.timer.TimerScreen
import com.example.pomodoro.ui.welcome.WelcomeDestination
import com.example.pomodoro.ui.welcome.WelcomeScreen

@Composable
fun PomodoroNavHost(
    modifier: Modifier = Modifier,
    updateThemeType: (Theme.ThemeType) -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        if (Option.WelcomeScreen.checked) {
            navController.navigate(WelcomeDestination.route)
            Option.WelcomeScreen.checked = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = /* StatsDestination.route */ TimerDestination.route,
            enterTransition = { fadeIn(tween(durationMillis = 200)) },
            exitTransition = { fadeOut(tween(durationMillis = 200)) }
        ) {
            composable(route = WelcomeDestination.route) {
                WelcomeScreen(
                    navigateToTimer = { navController.popBackStack() }
                )
            }
            composable(route = TimerDestination.route) {
                TimerScreen(
                    onSwitchOperatingModeClick = { context.pomodoroServiceIntent(Action.Switch) },
                    onResetTimerClick = { context.pomodoroServiceIntent(Action.Reset) },
                    updateChosenPhaseOnClick = { phase ->
                        context.pomodoroServiceIntent(Action.UpdatePhase, phase.name)
                    },
                    navigateToMenu = { navController.navigate(MenuDestination.route) }
                )
            }
            composable(route = MenuDestination.route) {
                MenuScreen(
                    updateThemeType = updateThemeType,
                    navigateToWelcome = {
                        navController.navigate(WelcomeDestination.route) {
                            popUpTo(WelcomeDestination.route)
                        }
                    },
                    navigateToTimer = { navController.popBackStack() }
                )
            }
            composable(route = StatsDestination.route) {
                StatsScreen(navigateToMenu = { navController.popBackStack() })
            }
        }
    }
}