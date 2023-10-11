package sgtmelon.scriptum.source.ui.screen.dialogs.permissions.deny

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.intent.SettingsIntent
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart

/**
 * Class for UI control of [MessageDialog] which open after [Permission.WriteExternalStorage] deny
 * to inform user. This dialog explains deny of backup import feature.
 */
class ImportDenyDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_import_deny,
    R.string.dialog_text_import_deny
), SettingsIntent {

    override fun positive() = trackIntent { super.positive() }

    override fun onPositiveResult() {
        super.onPositiveResult()
        assertSettingsOpen(context)
    }

    companion object {
        inline operator fun invoke(func: ImportDenyDialogUi.() -> Unit): ImportDenyDialogUi {
            return ImportDenyDialogUi().apply { assert() }.apply(func)
        }
    }
}