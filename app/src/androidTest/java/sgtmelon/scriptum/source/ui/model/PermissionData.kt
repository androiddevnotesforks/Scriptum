package sgtmelon.scriptum.source.ui.model

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission

object PermissionData {

    fun getTitle(context: Context, permission: Permission): String {
        val appName = context.getString(R.string.app_name)

        return when (permission) {
            Permission.PostNotifications -> "Allow $appName to send you notifications?"
            Permission.WriteExternalStorage -> TODO()
        }
    }

    // TODO later: different buttons by versions
    object Button {
        const val ALLOW = "Allow"
        const val DENY = "Don’t allow"
        const val NOT_ASK = "TODO"
    }
}