package com.example.pomodoro.data.options

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pomodoro.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Репозиторий для сохранения настроек приложения.
 * @param dataStore Хранилище данных.
 */
class OptionsRepository(private val dataStore: DataStore<Preferences>) {
    val themeTypeFlow: Flow<Theme.ThemeType> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            Theme.ThemeType.valueOf(
                value = preferences[THEME_TYPE_NAME] ?: Theme.currentThemeType.name
            )
        }

    val optionsFlow: Flow<Options> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, ERROR_MSG, it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            val nightMode: Boolean =
                preferences[NIGHT_MODE] ?: Option.NightMode.checked

            val autoBreakStart: Boolean =
                preferences[AUTO_BREAK_START] ?: Option.AutoBreakStart.checked

            val autoPomodoroStart: Boolean =
                preferences[AUTO_POMODORO_START] ?: Option.AutoPomodoroStart.checked

            Options(
                welcomeScreen = preferences[WELCOME_SCREEN] ?: Option.WelcomeScreen.checked,
                notificationSound = preferences[NOTIFY_SOUND] ?: Option.NotificationSound.checked,
                vibration = preferences[VIBRATION] ?: Option.Vibration.checked,
                keepTheScreenOn = preferences[KEEP_THE_SCREEN_ON] ?: Option.KeepTheScreenOn.checked,
                showTaskbar = preferences[SHOW_TASKBAR] ?: Option.ShowTaskbar.checked,
                fullScreenMode = preferences[FULLSCREEN_MODE] ?: Option.FullScreenMode.checked,
                autoBreakStart = autoBreakStart,
                autoPomodoroStart = autoPomodoroStart,
                nightMode = nightMode
            )
        }

    /**
     * Обновить настройки репозитория.
     * @param options Настройки приложения.
     */
    suspend fun updateOptions(options: Options) {
        dataStore.edit { preferences ->
            preferences[WELCOME_SCREEN] = options.welcomeScreen
            preferences[NOTIFY_SOUND] = options.notificationSound
            preferences[VIBRATION] = options.vibration
            preferences[KEEP_THE_SCREEN_ON] = options.keepTheScreenOn
            preferences[SHOW_TASKBAR] = options.showTaskbar
            preferences[FULLSCREEN_MODE] = options.fullScreenMode
            preferences[AUTO_BREAK_START] = options.autoBreakStart
            preferences[AUTO_POMODORO_START] = options.autoPomodoroStart
            preferences[NIGHT_MODE] = options.nightMode
        }
    }

    /**
     * Обновить название типа темы (цвет темы).
     * @param type Тип темы.
     */
    suspend fun updateThemeType(type: Theme.ThemeType) {
        dataStore.edit { preferences ->
            preferences[THEME_TYPE_NAME] = type.name
        }
    }

    private companion object {
        const val TAG: String = "OptionsRepo"
        const val ERROR_MSG: String = "Ошибка чтения данных dataStore."

        val WELCOME_SCREEN = booleanPreferencesKey(name = "welcome_screen")
        val NOTIFY_SOUND = booleanPreferencesKey(name = "notification_sound")
        val VIBRATION = booleanPreferencesKey(name = "vibration")
        val KEEP_THE_SCREEN_ON = booleanPreferencesKey(name = "keep_the_screen_on")
        val SHOW_TASKBAR = booleanPreferencesKey(name = "show_taskbar")
        val FULLSCREEN_MODE = booleanPreferencesKey(name = "fullscreen_mode")
        val AUTO_BREAK_START = booleanPreferencesKey(name = "auto_break_start")
        val AUTO_POMODORO_START = booleanPreferencesKey(name = "auto_pomodoro_start")
        val NIGHT_MODE = booleanPreferencesKey(name = "night_mode")
        val THEME_TYPE_NAME = stringPreferencesKey(name = "theme_type_name")
    }
}