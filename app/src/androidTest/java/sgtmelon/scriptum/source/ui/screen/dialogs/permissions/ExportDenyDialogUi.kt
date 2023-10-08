package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart

/**
 * Class for UI control of [MessageDialog] which open after [Permission.WriteExternalStorage] deny
 * to inform user. This dialog explains deny of backup export feature.
 */
class ExportDenyDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_export_deny,
    R.string.dialog_text_export_deny
)