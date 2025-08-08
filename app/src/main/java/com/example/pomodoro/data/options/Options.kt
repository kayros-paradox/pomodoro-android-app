package com.example.pomodoro.data.options

data class Options(
    val welcomeScreen: Boolean = true,
    val notificationSound: Boolean = true,
    val vibration: Boolean = true,
    val keepTheScreenOn: Boolean = true,
    val showTaskbar: Boolean = true,
    val fullScreenMode: Boolean = false,
    val autoBreakStart: Boolean = false,
    val autoPomodoroStart: Boolean = false,
    val nightMode: Boolean = false
)