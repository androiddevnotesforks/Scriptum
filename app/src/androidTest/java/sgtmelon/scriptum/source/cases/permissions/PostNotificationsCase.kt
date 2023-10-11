package sgtmelon.scriptum.source.cases.permissions

import android.content.Context
import org.junit.Assert.assertFalse
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.ui.model.exception.ApiPermissionException

/**
 * Case for describe behaviour of [Permission.PostNotifications].
 */
interface PostNotificationsCase {

    fun allow()

    fun denyInfo()

    fun denyInfoClose()

    fun denyInfoRotateWork()

    fun denyInfoRotateClose()

    fun throwOnLowApi() {
        if (!Permission.PostNotifications.isWorking) {
            throw ApiPermissionException(Permission.PostNotifications)
        }
    }

    fun assertPostNotificationsNotGranted(context: Context) {
        assertFalse(context.isPermissionGranted(Permission.PostNotifications))
    }
}