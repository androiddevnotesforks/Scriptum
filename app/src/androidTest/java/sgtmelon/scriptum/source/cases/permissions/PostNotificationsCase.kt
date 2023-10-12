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

    /** User grant permission - feature available for use. */
    fun allow()

    /** User deny permission, show information dialog + open settings. */
    fun denyInfo()

    /** User deny permission, show information dialog + close. */
    fun denyInfoClose()

    /** Same as [denyInfo] but with rotation. */
    fun denyInfoRotateWork()

    /** Same as [denyInfoClose] but with rotation. */
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