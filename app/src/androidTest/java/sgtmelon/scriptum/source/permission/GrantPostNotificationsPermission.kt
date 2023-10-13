package sgtmelon.scriptum.source.permission

import android.content.Context
import org.junit.Assert.assertTrue
import org.junit.Rule
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted
import sgtmelon.scriptum.source.ui.model.annotation.ApiPolicy
import sgtmelon.scriptum.source.ui.model.annotation.SpecificApi
import sgtmelon.test.cappuccino.rule.GrantPermissionApiRule

/**
 * Interface for fast [permission] grant. If api don't match (grant not possible) -> skip it.
 */
@SpecificApi(ApiPolicy.LIGHT)
interface GrantPostNotificationsPermission {

    private val permission get() = Permission.PostNotifications

    @get:Rule val postNotificationsGrantRule: GrantPermissionApiRule
        get() = GrantPermissionApiRule.grant(permission.isWorking, permission.value)

    fun assertPostNotificationsGranted(context: Context) {
        val isGranted = context.isPermissionGranted(permission) ?: return
        assertTrue(isGranted)
    }
}