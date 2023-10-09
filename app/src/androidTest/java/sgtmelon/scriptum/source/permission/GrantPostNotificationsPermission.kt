package sgtmelon.scriptum.source.permission

import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission

interface GrantPostNotificationsPermission {

    @get:Rule val postNotificationsGrantRule: GrantPermissionRule
        get() = GrantPermissionRule.grant(Permission.PostNotifications.value)

}