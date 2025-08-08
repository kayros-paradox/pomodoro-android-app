package com.example.pomodoro.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro.data.pomodoro.PomodoroRepository
import com.example.pomodoro.data.pomodoro.PomodoroUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TimerViewModel(applicationRepository: PomodoroRepository): ViewModel() {

    val uiStateFlow: StateFlow<PomodoroUiState> = applicationRepository.uiFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PomodoroUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}