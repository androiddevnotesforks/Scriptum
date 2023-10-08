package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart

/**
 * Class for UI control of [MessageDialog] which open before [Permission.WriteExternalStorage]
 * request. This dialog need to inform user about restrictions (related with melody search feature)
 * if he/she reject permission.
 */
class MelodyAccessDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_melody_permission,
    R.string.dialog_text_melody_permission
)