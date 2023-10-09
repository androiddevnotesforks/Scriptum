package sgtmelon.scriptum.source.cases.permissions

import android.content.Context
import org.junit.Assert.assertFalse
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase

/**
 * Case for describe behaviour of [Permission.PostNotifications].
 */
interface PostNotificationsCase : DialogRotateCase, DialogCloseCase {

    fun allow()

    fun deny()

    fun denyInfo()

    fun assertPostNotificationsNotGranted(context: Context) {
        assertFalse(context.isPermissionGranted(Permission.PostNotifications))
    }
}