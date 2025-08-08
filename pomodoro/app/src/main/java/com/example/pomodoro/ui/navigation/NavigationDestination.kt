package com.example.pomodoro.ui.navigation

import androidx.compose.runtime.Immutable

@Immutable
interface NavigationDestination {
    val route: String
}