package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart

/**
 * Class for UI control of [MessageDialog] which open after [Permission.Notifications] deny
 * to inform user.
 */
class NotificationsDenyDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_notification_deny,
    R.string.dialog_text_notification_deny
)