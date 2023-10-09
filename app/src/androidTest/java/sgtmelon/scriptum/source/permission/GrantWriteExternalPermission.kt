package sgtmelon.scriptum.source.permission

import android.content.Context
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.infrastructure.utils.extensions.isPermissionGranted

interface GrantWriteExternalPermission {

    @get:Rule val writeExternalGrantRule: GrantPermissionRule
        get() = GrantPermissionRule.grant(Permission.WriteExternalStorage.value)

    fun assertWriteExternalGranted(context: Context) {
        assertTrue(context.isPermissionGranted(Permission.WriteExternalStorage))
    }
}