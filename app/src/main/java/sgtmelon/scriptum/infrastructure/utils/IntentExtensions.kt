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

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = uri

    return intent
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

/**
 * If MARKET_URL is not available, them try open it via browser.
 */
fun Context.startMarketActivity(toast: ToastDelegator) {
    try {
        val intent = getUrlIntent(BuildConfig.MARKET_URL.plus(packageName))
        if (intent != null) {
            startActivity(intent)
        } else {
            toast.show(this, R.string.error_something_wrong)
        }
    } catch (e: Throwable) {
        e.record()
        startActivitySafe(getUrlIntent(BuildConfig.BROWSER_URL.plus(packageName)), toast)
    }
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
