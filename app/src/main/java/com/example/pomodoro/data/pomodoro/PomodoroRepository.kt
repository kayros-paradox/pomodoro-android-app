package com.example.pomodoro.data.pomodoro

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pomodoro.data.timer.OperatingMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PomodoroRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val defaultRestInterval: Int = 4

    val restIntervalFLow: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[REST_INTERVAL] ?: defaultRestInterval
        }

    val uiFlow: Flow<PomodoroUiState> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            PomodoroUiState(
                currentSeconds = preferences[CURRENT_SECONDS]
                    ?: (PomodoroPhase.POMODORO.minutes * 60),
                chosenPhase = PomodoroPhase.valueOf(
                    value = preferences[CHOSEN_PHASE_NAME] ?: PomodoroPhase.POMODORO.name),
                currentPhase = PomodoroPhase.valueOf(
                    value = preferences[CURRENT_PHASE_NAME] ?: PomodoroPhase.POMODORO.name),
                operatingMode = OperatingMode.valueOf(
                    value = preferences[OPERATING_MODE_NAME] ?: OperatingMode.STOPPED.name)
            )
        }

    suspend fun updateUiState(uiState: PomodoroUiState) {
        dataStore.edit { preferences ->
            preferences[CURRENT_SECONDS] = uiState.currentSeconds
            preferences[CHOSEN_PHASE_NAME] = uiState.chosenPhase.name
            preferences[CURRENT_PHASE_NAME] = uiState.currentPhase.name
            preferences[OPERATING_MODE_NAME] = uiState.operatingMode.name
        }
    }

    suspend fun updateRestInterval(interval: Int) {
        dataStore.edit { preferences ->
            preferences[REST_INTERVAL] = interval
        }
    }

    private companion object {
        const val TAG: String = "ApplicationRepo"
        const val ERROR_MSG: String = "Ошибка чтения данных dataStore."

        val CURRENT_SECONDS = intPreferencesKey(name = "current_seconds")
        val CHOSEN_PHASE_NAME = stringPreferencesKey(name = "chosen_phase_name")
        val CURRENT_PHASE_NAME = stringPreferencesKey(name = "current_phase_name")
        val OPERATING_MODE_NAME = stringPreferencesKey(name = "operating_mode")
        val REST_INTERVAL = intPreferencesKey(name = "rest_interval")
    }
}