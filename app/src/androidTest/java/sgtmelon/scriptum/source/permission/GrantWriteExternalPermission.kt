package sgtmelon.scriptum.source.permission

import android.Manifest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule

interface GrantWriteExternalPermission {

    @get:Rule val writeExternalPermission: GrantPermissionRule
        get() = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

}