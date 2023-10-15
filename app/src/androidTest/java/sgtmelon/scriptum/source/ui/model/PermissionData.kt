package sgtmelon.scriptum.source.ui.model

import android.content.Context
import android.os.Build
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission

object PermissionData {

    fun getTitle(context: Context, permission: Permission): String {
        val appName = context.getString(R.string.app_name)

        return when (permission) {
            Permission.PostNotifications -> "Allow $appName to send you notifications?"
            Permission.WriteExternalStorage -> {
                "Allow $appName to access photos, media, and files on your device?"
            }
        }
    }

    fun getAllowButton(): String {
        return when (BuildProvider.Version.current) {
            Build.VERSION_CODES.TIRAMISU -> "Allow" // Android 13 / API=33
            Build.VERSION_CODES.O -> "ALLOW"        // Android 08 / API=26
            else -> TODO("Please, specify your version")
        }
    }

    fun getDenyButton(): String {
        return when (BuildProvider.Version.current) {
            Build.VERSION_CODES.TIRAMISU -> "Don’t allow" // Android 13 / API=33
            Build.VERSION_CODES.O -> "DENY"               // Android 08 / API=26
            else -> TODO("Please, specify your version")
        }
    }

    fun getNotAskButton(): String {
        return when (BuildProvider.Version.current) {
            Build.VERSION_CODES.TIRAMISU -> throw IllegalAccessException("Tiramisu don't have this button")
            Build.VERSION_CODES.O -> "Don't ask again"
            else -> TODO("Please, specify your version")
        }
    }
}