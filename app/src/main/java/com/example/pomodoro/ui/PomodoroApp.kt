package com.example.pomodoro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pomodoro.ui.navigation.PomodoroNavHost
import com.example.pomodoro.ui.theme.Theme

@Composable
fun PomodoroApp(
    updateThemeType: (Theme.ThemeType) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    PomodoroNavHost(
        updateThemeType = updateThemeType,
        navController = navController
    )
}