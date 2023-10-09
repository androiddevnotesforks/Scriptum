package sgtmelon.scriptum.source.permission

import android.content.Context
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted

interface GrantPostNotificationsPermission {

    @get:Rule val postNotificationsGrantRule: GrantPermissionRule
        get() = GrantPermissionRule.grant(Permission.PostNotifications.value)

    fun assertPostNotificationsGranted(context: Context) {
        assertTrue(context.isPermissionGranted(Permission.PostNotifications))
    }
}