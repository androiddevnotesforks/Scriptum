package sgtmelon.scriptum.source.cases.permissions

import android.content.Context
import org.junit.Assert.assertFalse
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.ui.model.annotation.ApiPolicy
import sgtmelon.scriptum.source.ui.model.annotation.SpecificApi
import sgtmelon.scriptum.source.ui.model.exception.ApiPermissionException

/**
 * Case for describe behaviour of [Permission.PostNotifications] and note bind feature.
 */
@SpecificApi(ApiPolicy.STRICT)
interface BindNotePermissionCase {

    private val permission: Permission get() = Permission.PostNotifications

    /** User grant permission - feature available to use. */
    fun allow()

    /** User deny permission, show information dialog + open settings. */
    fun denyInfo()

    /** User deny permission, show information dialog + close. */
    fun denyInfoClose()

    /** Same as [denyInfo] but with rotation. */
    fun denyInfoRotateWork()

    /** Same as [denyInfoClose] but with rotation. */
    fun denyInfoRotateClose()

    fun throwOnWrongApi() {
        if (!permission.isWorking) {
            throw ApiPermissionException(permission)
        }
    }

    fun assertPostNotificationsNotGranted(context: Context) {
        val isGranted = context.isPermissionGranted(permission) ?: return
        assertFalse(isGranted)
    }
}