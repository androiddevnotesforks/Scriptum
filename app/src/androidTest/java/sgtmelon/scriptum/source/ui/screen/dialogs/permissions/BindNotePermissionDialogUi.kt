package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import sgtmelon.scriptum.source.ui.parts.dialog.permission.realization.PostNotificationsPermissionDialogUi

/**
 * Realization of post notification permission dialog with ability to check result.
 */
class BindNotePermissionDialogUi : PostNotificationsPermissionDialogUi() {

    // TODO assertion in status bar

    fun allow(onResult: () -> Unit) {
        super.allow()
        onResult()
    }

    companion object {
        inline operator fun invoke(
            func: BindNotePermissionDialogUi.() -> Unit
        ): BindNotePermissionDialogUi {
            return BindNotePermissionDialogUi().apply { assert() }.apply(func)
        }
    }
}