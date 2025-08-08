package com.example.pomodoro.data.options

import androidx.annotation.StringRes
import com.example.pomodoro.R

enum class Option(
    @StringRes val nameId: Int,
    @StringRes val descriptionId: Int? = null,
    var checked: Boolean = true,
    var onSwitch: () -> Unit = {}
) {
    WelcomeScreen(R.string.option_welcome_screen),
    NotificationSound(R.string.option_notification_sound),
    Vibration(R.string.option_vibration),
    KeepTheScreenOn(R.string.option_keep_the_screen_on),
    ShowTaskbar(R.string.option_show_taskbar),
    FullScreenMode(
        nameId = R.string.option_fullscreen_mode,
        checked = false
    ),

    AutoBreakStart(
        nameId = R.string.option_auto_break_start,
        descriptionId = R.string.option_auto_break_start_description,
        checked = false
    ),

    AutoPomodoroStart(
        nameId = R.string.option_auto_pomodoro_start,
        descriptionId = R.string.option_auto_pomodoro_start_description,
        checked = false
    ),

    NightMode(
        nameId = R.string.option_night_mode,
        checked = false
    ),

}

enum class OptionValue(var data: Int) {
    RestInterval(data = 4)
}