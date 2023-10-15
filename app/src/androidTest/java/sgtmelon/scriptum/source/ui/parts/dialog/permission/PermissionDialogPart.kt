package sgtmelon.scriptum.source.ui.parts.dialog.permission

import androidx.annotation.CallSuper
import androidx.test.uiautomator.UiObject
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

    private val allowButton: UiObject = getObject(PermissionData.getAllowButton())
    private val denyButton: UiObject = getObject(PermissionData.getDenyButton())

    /** This button is optional, one time it exist, another time - not. */
    private val notAskButton: UiObject = getObject(PermissionData.getNotAskButton())

    @CallSuper open fun allow(): Unit = run { allowButton.click() }
    @CallSuper open fun deny(): Unit = run { denyButton.click() }

    @CallSuper open fun notAsk() {
        assertTrue(notAskButton.exists())
        notAskButton.click()
        denyButton.click()
    }

    @CallSuper open fun assert() {
        assertTrue(titleText.waitForExists(AWAIT_TIMEOUT))

        assertTrue(allowButton.exists())
        assertTrue(denyButton.exists())
    }
}