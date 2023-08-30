package sgtmelon.scriptum.source.permission

import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission

interface GrantWriteExternalPermission {

    @get:Rule val writeExternalGrantRule: GrantPermissionRule
        get() = GrantPermissionRule.grant(Permission.WriteExternalStorage.value)

}