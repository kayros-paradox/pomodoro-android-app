package com.example.pomodoro.data.timer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.pomodoro.R

enum class OperatingMode(
    @DrawableRes val iconId: Int,
    @StringRes val nameId: Int
) {
    PAUSED(R.drawable.pause, R.string.paused),
    STOPPED(R.drawable.play_arrow, R.string.stopped),
    ACTIVE(R.drawable.play_arrow, R.string.active);

    fun isActive(): Boolean = this == ACTIVE
    fun isStopped(): Boolean = this == STOPPED
    fun isPaused(): Boolean = this == PAUSED
}
