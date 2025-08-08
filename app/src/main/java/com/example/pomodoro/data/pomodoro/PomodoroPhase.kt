package com.example.pomodoro.data.pomodoro

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.pomodoro.R

enum class PomodoroPhase(
    @StringRes val nameId: Int,
    @DrawableRes val unChosenIconId: Int,
    @DrawableRes val chosenIconId: Int = unChosenIconId,
    var minutes: Int = 0
) {
    BREAK(
        nameId = R.string.mode_break,
        unChosenIconId = R.drawable.timer,
        chosenIconId = R.drawable.timer_filled,
        minutes = 1
    ),

    POMODORO(
        nameId = R.string.mode_pomodoro,
        unChosenIconId = R.drawable.nutrition,
        chosenIconId = R.drawable.nutrition_filled,
        minutes = 25
    ),

    REST(
        nameId = R.string.mode_rest,
        unChosenIconId = R.drawable.self_improvement,
        minutes = 15
    )
}
