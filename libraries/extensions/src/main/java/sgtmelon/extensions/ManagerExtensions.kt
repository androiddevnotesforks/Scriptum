package sgtmelon.extensions

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager

fun Context.getNotificationService(): NotificationManager? {
    return getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
}

fun Context.getAlarmService(): AlarmManager? {
    return getSystemService(Context.ALARM_SERVICE) as? AlarmManager
}

fun Context.getPowerService(): PowerManager? {
    return getSystemService(Context.POWER_SERVICE) as? PowerManager
}

fun Context.getInputService(): InputMethodManager? {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
}

fun Context.getClipboardService(): ClipboardManager? {
    return getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
}

fun Context.getAudioService(): AudioManager? {
    return getSystemService(Context.AUDIO_SERVICE) as? AudioManager
}

fun Context.getVibratorService(): Vibrator? {
    return getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
}