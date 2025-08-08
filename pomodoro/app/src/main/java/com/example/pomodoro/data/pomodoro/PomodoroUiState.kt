package com.example.pomodoro.data.pomodoro

import com.example.pomodoro.data.timer.OperatingMode

data class PomodoroUiState(
    val operatingMode: OperatingMode = OperatingMode.STOPPED,
    val chosenPhase: PomodoroPhase = PomodoroPhase.POMODORO,
    val currentPhase: PomodoroPhase = chosenPhase,
    val currentSeconds: Int = chosenPhase.minutes * 60,
    val restInterval: Int = 4
) {
    val toString: String
        get() = "${currentSeconds/60}:${(currentSeconds%60)/10}${(currentSeconds%60)%10}"

    val toLocal: LocalPomodoroUiState
        get() = LocalPomodoroUiState(
            currentSeconds = currentSeconds,
            chosenPhase = chosenPhase,
            currentPhase = currentPhase,
            operatingMode = operatingMode,
            restInterval = restInterval
        )
}

data class LocalPomodoroUiState (
    var operatingMode: OperatingMode = OperatingMode.STOPPED,
    var chosenPhase: PomodoroPhase = PomodoroPhase.POMODORO,
    var currentPhase: PomodoroPhase = chosenPhase,
    var currentSeconds: Int = chosenPhase.minutes * 60,
    var restInterval: Int = 4
) {
    val toString: String
        get() = "${currentSeconds/60}:${(currentSeconds%60)/10}${(currentSeconds%60)%10}"

    val toExternal: PomodoroUiState
        get() = PomodoroUiState(
            currentSeconds = currentSeconds,
            chosenPhase = chosenPhase,
            currentPhase = currentPhase,
            operatingMode = operatingMode,
            restInterval = restInterval
        )
}

