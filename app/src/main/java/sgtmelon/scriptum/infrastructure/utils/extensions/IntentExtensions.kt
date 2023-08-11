package sgtmelon.scriptum.infrastructure.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.infrastructure.converter.UriConverter
import sgtmelon.scriptum.infrastructure.model.key.AppError
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator

fun getPickFileIntent(): Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/*"
    }
}

private fun getUrlIntent(url: String): Intent? {
    val uri = UriConverter().toUri(url) ?: return null
    return Intent(Intent.ACTION_VIEW, uri)
}

private fun Context.getSettingsIntent(): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)

    return intent
}

private fun Context.getSettingsChannelIntent(@StringRes id: Int): Intent {
    return Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        .putExtra(Settings.EXTRA_CHANNEL_ID, getString(id))
}

/** Only email apps should handle this. */
private fun Context.getEmailIntent(email: String, @StringRes subjectId: Int): Intent {
    return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        .putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        .putExtra(Intent.EXTRA_SUBJECT, getString(subjectId))
}

fun Context.startUrlActivity(url: String, toast: ToastDelegator?) {
    startActivitySafe(getUrlIntent(url), toast)
}

fun Context.startSettingsActivity(toast: ToastDelegator?) {
    startActivitySafe(getSettingsIntent(), toast)
}

fun Context.startSettingsChannelActivity(toast: ToastDelegator?, @StringRes id: Int) {
    startActivitySafe(getSettingsChannelIntent(id), toast)
}

fun Context.startMarketActivity(toast: ToastDelegator?) {
    startActivitySafe(getUrlIntent(BuildConfig.BROWSER_URL), toast)
}

fun Context.startEmailActivity(toast: ToastDelegator?, email: String, @StringRes subjectId: Int) {
    startActivitySafe(getEmailIntent(email, subjectId), toast)
}

private fun Context.startActivitySafe(intent: Intent?, toast: ToastDelegator?) {
    if (intent != null) {
        try {
            startActivity(intent)
        } catch (e: Throwable) {
            e.record()
            toast?.show(context = this, AppError.StartActivity)
        }
    } else {
        toast?.show(context = this, AppError.Unknown)
    }
}
