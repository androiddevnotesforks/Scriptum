package sgtmelon.scriptum.source.ui.parts.dialog.permission

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart

/**
 * Parent class for UI control of system permission dialogs.
 */
abstract class PermissionDialogPart(private val permission: Permission): UiPart(),
    DialogUi {

    private val allowButton: Matcher<View> = TODO()
    private val dontAskButton: Matcher<View> = TODO()
    private val denyButton: Matcher<View> = TODO()

    fun assert() {
        TODO()
    }
}