package com.example.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.modules.OptionsModule
import com.example.pomodoro.service.PomodoroService
import com.example.pomodoro.service.pomodoroServiceIntent
import com.example.pomodoro.ui.PomodoroApp
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.example.pomodoro.ui.theme.Theme

class MainActivity : ComponentActivity() {
    private val controller: WindowInsetsControllerCompat by lazy {
        WindowCompat.getInsetsController(window, window.decorView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        requestPermissions()
        applicationContext.pomodoroServiceIntent(PomodoroService.Action.ShowNotification)
        setContent {
            var darkTheme by remember { mutableStateOf(Option.NightMode.checked) }
            var themeType by remember { mutableStateOf(Theme.currentThemeType) }

            changeFullScreenMode()

            SetOptionsOnSwitch(
                changeDarkTheme = { darkTheme = it },
                changeFullScreenMode = { changeFullScreenMode() }
            )

            PomodoroTheme(darkTheme = darkTheme, themeType = themeType) {
                PomodoroApp(updateThemeType = { themeType = it })
            }
        }
    }

    override fun onStop() {
        super.onStop()
        OptionsModule.saveOptions(context = applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        OptionsModule.saveOptions(context = applicationContext)
        applicationContext.pomodoroServiceIntent(PomodoroService.Action.Close)
    }

    override fun onResume() {
        super.onResume()
        changeFullScreenMode()
        applicationContext.pomodoroServiceIntent(PomodoroService.Action.ShowNotification)
    }

    private fun changeFullScreenMode() {
        if (Option.FullScreenMode.checked) {
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.navigationBars())
        } else {
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            controller.show(WindowInsetsCompat.Type.navigationBars())
        }
    }
}
