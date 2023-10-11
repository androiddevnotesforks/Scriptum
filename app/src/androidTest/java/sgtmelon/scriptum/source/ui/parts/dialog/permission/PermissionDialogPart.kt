package sgtmelon.scriptum.source.ui.parts.dialog.permission

import androidx.annotation.CallSuper
import androidx.test.uiautomator.UiObject
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.model.PermissionData
import sgtmelon.scriptum.source.ui.parts.UiAutomatorPart

/**
 * Parent class for UI control of system permission dialogs.
 */
abstract class PermissionDialogPart(protected val permission: Permission): UiAutomatorPart(),
    DialogUi {

    private val titleText: UiObject = getObject(PermissionData.getTitle(context, permission))

    abstract val allowButton: UiObject
    abstract val denyButton: UiObject

    /** This button is optional, one time it exist, another time - not. */
    abstract val notAskButton: UiObject?

    fun allow() = allowButton.click()
    fun deny() = denyButton.click()

    fun notAsk() {
        assertNotNull(notAskButton)
        notAskButton?.click()
    }

    @CallSuper open fun assert() {
        assertTrue(titleText.waitForExists(AWAIT_TIMEOUT))
    }
}