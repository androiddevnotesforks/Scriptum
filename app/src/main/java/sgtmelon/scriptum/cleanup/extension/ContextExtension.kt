package sgtmelon.scriptum.cleanup.extension

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator
import sgtmelon.scriptum.infrastructure.utils.record

fun Context.getDisplayedTheme(): ThemeDisplayed? {
    val uiMode = resources.configuration.uiMode

    return when (uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> ThemeDisplayed.LIGHT
        Configuration.UI_MODE_NIGHT_YES -> ThemeDisplayed.DARK
        else -> run {
            NullPointerException("Unknown configuration! Received null for uiMode=$uiMode").record()
            return@run null
        }
    }
}

inline fun Context.sendTo(place: String, command: String, extras: Intent.() -> Unit = {}) {
    sendBroadcast(Intent(place).apply {
        putExtra(ReceiverData.Values.COMMAND, command)
        putExtras(Intent().apply(extras))
    })
}

inline fun Context.sendTo(places: List<String>, command: String, extras: Intent.() -> Unit = {}) {
    for (place in places) {
        sendTo(place, command, extras)
    }
}

fun Context.isPortraitMode(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

inline fun <reified F : Fragment> FragmentManager.getFragmentByTag(tag: String): F? {
    return findFragmentByTag(tag) as? F
}

//region Intent functions

internal fun getSiteIntent(url: String): Intent? {
    val uri = UriConverter().toUri(url) ?: return null

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri

    return intent
}

/**
 * Intent for open application settings.
 */
internal fun Context.getSettingsIntent(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)

    return intent
}

internal fun Context.startActivitySafe(intent: Intent?, toast: ToastDelegator) {
    if (intent != null) {
        if (!startActivitySafe(intent)) {
            toast.show(context = this, R.string.error_start_activity)
        }
    } else {
        toast.show(context = this, R.string.error_something_wrong)
    }
}

private fun Context.startActivitySafe(intent: Intent): Boolean {
    return try {
        startActivity(intent)
        true
    } catch (e: Throwable) {
        e.record()
        false
    }
}

//endregion