package sgtmelon.scriptum.extension

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.ReceiverData

fun Context.getCompatColor(@ColorRes id: Int) = let { ContextCompat.getColor(it, id) }

@Theme
fun Context.getAppTheme(): Int? {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> Theme.LIGHT
        Configuration.UI_MODE_NIGHT_YES -> Theme.DARK
        else -> null
    }
}

//region Get resource value

@ColorInt
fun Context.getColorAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return getCompatColor(typedValue.resourceId)
}

@DimenRes
fun Context.getDimenAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return typedValue.resourceId
}

fun Context.getDimen(value: Float): Int {
    val unit = TypedValue.COMPLEX_UNIT_DIP
    val metrics = resources.displayMetrics

    return TypedValue.applyDimension(unit, value, metrics).toInt()
}

//endregion

fun Context.sendTo(place: String, command: String, extras: Intent.() -> Unit = {}) {
    sendBroadcast(Intent(place).apply {
        putExtra(ReceiverData.Values.COMMAND, command)
        putExtras(Intent().apply(extras))
    })
}

fun Context.isPortraitMode(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

//region Get system services

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

//endregion

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}

fun getUrlIntent(url: String): Intent? {
    val uri = url.toUri() ?: return null

    return Intent(Intent.ACTION_VIEW).apply { data = uri }
}