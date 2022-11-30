package sgtmelon.scriptum.infrastructure.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

fun Context.startUrlActivity(url: String, toast: ToastDelegator) {
    startActivitySafe(getUrlIntent(url), toast)
}

fun Context.startSettingsActivity(toast: ToastDelegator) {
    startActivitySafe(getSettingsIntent(), toast)
}

fun Context.startMarketActivity(toast: ToastDelegator) {
    startActivitySafe(getUrlIntent(BuildConfig.BROWSER_URL), toast)
}

private fun Context.startActivitySafe(intent: Intent?, toast: ToastDelegator) {
    if (intent != null) {
        try {
            startActivity(intent)
        } catch (e: Throwable) {
            e.record()
            toast.show(context = this, R.string.error_start_activity)
        }
    } else {
        toast.show(context = this, R.string.error_something_wrong)
    }
}
