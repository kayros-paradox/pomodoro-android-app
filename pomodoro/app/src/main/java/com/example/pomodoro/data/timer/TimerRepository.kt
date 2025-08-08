package com.example.pomodoro.data.timer

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Репозиторий для изменения длительности фаз помидора.
 * @param dataStore Хранилище данных.
 */
class TimerRepository(private val dataStore: DataStore<Preferences>) {

    val timerOptionsFlow: Flow<List<Int>> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            listOf(
                preferences[BREAK_PHASE_MINUTES] ?: DEFAULT_BREAK_PHASE_MINUTES,
                preferences[POMODORO_PHASE_MINUTES] ?: DEFAULT_POMODORO_PHASE_MINUTES,
                preferences[REST_PHASE_MINUTES] ?: DEFAULT_REST_PHASE_MINUTES
            )
        }

    /**
     * Обновить настройки таймера [длительность фаз].
     * @param breakPhaseMinutes Количество минут фазы перерыва.
     * @param pomodoroPhaseMinutes Количество минут фазы помидора.
     * @param restPhaseMinutes Количество минут фазы отдыха.
     */
    suspend fun updateTimerOptions(
        breakPhaseMinutes: Int,
        pomodoroPhaseMinutes: Int,
        restPhaseMinutes: Int
    ) {
        dataStore.edit { preferences ->
            preferences[BREAK_PHASE_MINUTES] = breakPhaseMinutes
            preferences[POMODORO_PHASE_MINUTES] = pomodoroPhaseMinutes
            preferences[REST_PHASE_MINUTES] = restPhaseMinutes
        }
    }

    private companion object {
        const val TAG: String = "TimerRepo"
        const val ERROR_MSG: String = "Ошибка чтения данных dataStore."

        const val DEFAULT_BREAK_PHASE_MINUTES: Int = 5
        const val DEFAULT_POMODORO_PHASE_MINUTES: Int = 25
        const val DEFAULT_REST_PHASE_MINUTES: Int = 15

        val BREAK_PHASE_MINUTES = intPreferencesKey(name = "break_phase_minutes")
        val POMODORO_PHASE_MINUTES = intPreferencesKey(name = "pomodoro_phase_minutes")
        val REST_PHASE_MINUTES = intPreferencesKey(name = "rest_phase_minutes")
    }
}