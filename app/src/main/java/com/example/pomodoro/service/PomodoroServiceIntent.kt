package com.example.pomodoro.service

import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * Намерение для сервиса помодоро таймера.
 * @param action Enum action намерения.
 * @param extraValue Передаваемое string значение для действия (необязательный параметр).
 */
fun Context.pomodoroServiceIntent(
    action: PomodoroService.Action,
    extraValue: String = ""
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        applicationContext.startForegroundService(  /* service intent */
            Intent(this, PomodoroService::class.java).apply {
                this.action = action.toString()
                this.putExtra(PomodoroService.extraName, extraValue)
            }
        )
    } else {
        applicationContext.startService(    /* service intent */
            Intent(this, PomodoroService::class.java).apply {
                this.action = action.toString()
                this.putExtra(PomodoroService.extraName, extraValue)
            }
        )
    }
}