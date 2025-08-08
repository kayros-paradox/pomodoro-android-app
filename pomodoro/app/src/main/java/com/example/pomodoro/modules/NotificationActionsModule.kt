package com.example.pomodoro.modules

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.example.pomodoro.R
import com.example.pomodoro.service.PomodoroService

/**
 * Содержит методы для инициализации и получения намерений для уведомлений.
 */
object NotificationActionsModule {
    /**
     * Намерение: клик на уведомление.
     * @param context Контекст сервиса.
     * @return Возвращает PendingIntent.
     */
    fun provideClickPendingIntent(context: Context): PendingIntent {
        val flag = PendingIntent.FLAG_IMMUTABLE
        val requestCode = 0
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        return PendingIntent.getActivity(context, requestCode, intent, flag)
    }

    /**
     * Action: запуск таймера.
     * @param context Контекст сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    fun provideActionStart(context: Context): NotificationCompat.Action {
        return provideServiceAction(
            context = context,
            iconId = R.drawable.play_arrow,
            nameId = R.string.notification_button_start,
            serviceAction = PomodoroService.Action.Start
        )
    }

    /**
     * Action: прекращение работы приложения.
     * @param context Контекст сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    fun provideActionClose(context: Context): NotificationCompat.Action {
        return provideServiceAction(
            context = context,
            iconId = R.drawable.close,
            nameId = R.string.notification_button_close,
            serviceAction = PomodoroService.Action.Close
        )
    }

    /**
     * Action: приостановка таймера.
     * @param context Контекст сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    fun provideActionPause(context: Context): NotificationCompat.Action {
        return provideServiceAction(
            context = context,
            iconId = R.drawable.pause,
            nameId = R.string.notification_button_pause,
            serviceAction = PomodoroService.Action.Pause
        )
    }

    /**
     * Action: продолжение работы таймера.
     * @param context Контекст сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    fun provideActionResume(context: Context): NotificationCompat.Action {
        return provideServiceAction(
            context = context,
            iconId = R.drawable.play_arrow,
            nameId = R.string.notification_button_resume,
            serviceAction = PomodoroService.Action.Resume
        )
    }

    /**
     * Action: сброс таймера (полная остановка).
     * @param context Контекст сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    fun provideActionReset(context: Context): NotificationCompat.Action {
        return provideServiceAction(
            context = context,
            iconId = R.drawable.timer,
            nameId = R.string.notification_button_reset,
            serviceAction = PomodoroService.Action.Reset
        )
    }

    /**
     * Сборка данных в класс NotificationActionsModule.Action.
     * @param iconId Id иконки кнопки у action.
     * @param nameId Id текста кнопки action.
     * @param context Контекст сервиса.
     * @param serviceAction Enum action из pomodoro сервиса.
     * @return Возвращает NotificationActionsModule.Action.
     */
    private fun provideServiceAction(
        @DrawableRes iconId: Int,
        @StringRes nameId: Int,
        context: Context,
        serviceAction: PomodoroService.Action,
    ): NotificationCompat.Action {
        val requestCode = 0
        val flag = PendingIntent.FLAG_IMMUTABLE
        val intent = Intent(context, PomodoroService::class.java)
        intent.setAction(serviceAction.toString())

        return NotificationCompat.Action(
            iconId,
            context.getString(nameId),
            PendingIntent.getService(context, requestCode, intent, flag)
        )
    }
}
