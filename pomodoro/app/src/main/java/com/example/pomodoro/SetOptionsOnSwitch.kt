package com.example.pomodoro

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.pomodoro.data.options.Option
import com.example.pomodoro.modules.VibrationModule

@Composable
fun SetOptionsOnSwitch(
    changeDarkTheme: (Boolean) -> Unit,
    changeFullScreenMode: () -> Unit
) {
    val context = LocalContext.current

    Option.FullScreenMode.onSwitch = changeFullScreenMode

    Option.Vibration.onSwitch = {
        if (Option.Vibration.checked)
            VibrationModule.vibrate()
    }

    Option.KeepTheScreenOn.onSwitch = {
        val activity: Activity = context as Activity
        val flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        val checked = Option.KeepTheScreenOn.checked

        when {
            checked -> activity.window.addFlags(flag)
            else -> activity.window.clearFlags(flag)
        }
    }

    Option.NightMode.onSwitch = {
        changeDarkTheme(Option.NightMode.checked)
    }
}