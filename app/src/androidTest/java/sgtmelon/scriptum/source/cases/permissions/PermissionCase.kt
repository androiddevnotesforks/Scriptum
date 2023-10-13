package sgtmelon.scriptum.source.cases.permissions

import android.content.Context
import org.junit.Assert.assertFalse
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.ui.model.annotation.ApiPolicy
import sgtmelon.scriptum.source.ui.model.annotation.SpecificApi
import sgtmelon.scriptum.source.ui.model.exception.ApiPermissionException

/**
 * Base interface for all permission test cases.
 */
@SpecificApi(ApiPolicy.STRICT)
interface PermissionCase {

    val permission: Permission

    fun throwOnWrongApi() {
        if (!permission.isWorking) {
            throw ApiPermissionException(permission)
        }
    }

    fun assertPermissionNotGranted(context: Context) {
        val isGranted = context.isPermissionGranted(permission) ?: return
        assertFalse(isGranted)
    }
}