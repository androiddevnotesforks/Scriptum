package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import android.Manifest
import androidx.test.uiautomator.UiObject
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.model.PermissionData
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionDialogPart

/**
 * Class for UI control of system [Manifest.permission.POST_NOTIFICATIONS] permission dialog.
 */
class PostNotificationsPermissionDialogUi : PermissionDialogPart(Permission.PostNotifications) {

    override val allowButton: UiObject = getObject(PermissionData.Button.ALLOW)
    override val denyButton: UiObject = getObject(PermissionData.Button.DENY)

    override val notAskButton: UiObject? = null

    override fun assert() {
        super.assert()

        assertTrue(allowButton.exists())
        assertTrue(denyButton.exists())
    }

    companion object {
        inline operator fun invoke(
            func: PostNotificationsPermissionDialogUi.() -> Unit
        ): PostNotificationsPermissionDialogUi {

            return PostNotificationsPermissionDialogUi().apply { assert() }.apply(func)
        }
    }
}