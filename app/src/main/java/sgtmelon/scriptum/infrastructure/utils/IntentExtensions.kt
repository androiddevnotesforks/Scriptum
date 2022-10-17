package sgtmelon.scriptum.infrastructure.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator

private fun getSiteIntent(url: String): Intent? {
    val uri = UriConverter().toUri(url) ?: return null

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri

    return intent
}

private fun Context.getSettingsIntent(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)

    return intent
}

fun Context.startSiteActivitySafe(url: String, toast: ToastDelegator) {
    startActivitySafe(getSiteIntent(url), toast)
}

fun Context.startSettingsActivitySafe(toast: ToastDelegator) {
    startActivitySafe(getSettingsIntent(), toast)
}

/**
 * If MARKET_URL is not available, them try open it via browser.
 */
fun Context.startMarketActivitySafe(toast: ToastDelegator) {
    try {
        val intent = getSiteIntent(BuildConfig.MARKET_URL.plus(packageName))
        if (intent != null) {
            startActivity(intent)
        } else {
            toast.show(this, R.string.error_something_wrong)
        }
    } catch (e: ActivityNotFoundException) {
        e.record()
        startActivitySafe(getSiteIntent(BuildConfig.BROWSER_URL.plus(packageName)), toast)
    }
}

private fun Context.startActivitySafe(intent: Intent?, toast: ToastDelegator) {
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
