package sgtmelon.scriptum.infrastructure.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator


private fun getUrlIntent(url: String): Intent? {
    val uri = UriConverter().toUri(url) ?: return null
    return Intent(Intent.ACTION_VIEW, uri)
}

private fun Context.getSettingsIntent(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)

    return intent
}

private fun Context.getSettingsChannelIntent(@StringRes channelId: Int): Intent {
    return Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        .putExtra(Settings.EXTRA_CHANNEL_ID, getString(channelId))
}

fun Context.startUrlActivity(url: String, toast: ToastDelegator?) {
    startActivitySafe(getUrlIntent(url), toast)
}

fun Context.startSettingsActivity(toast: ToastDelegator?) {
    startActivitySafe(getSettingsIntent(), toast)
}

fun Context.startSettingsChannelActivity(@StringRes channelId: Int, toast: ToastDelegator?) {
    startActivitySafe(getSettingsChannelIntent(channelId), toast)
}

fun Context.startMarketActivity(toast: ToastDelegator?) {
    startActivitySafe(getUrlIntent(BuildConfig.BROWSER_URL), toast)
}

private fun Context.startActivitySafe(intent: Intent?, toast: ToastDelegator?) {
    if (intent != null) {
        try {
            startActivity(intent)
        } catch (e: Throwable) {
            e.record()
            toast?.show(context = this, R.string.error_start_activity)
        }
    } else {
        toast?.show(context = this, R.string.error_something_wrong)
    }
}
