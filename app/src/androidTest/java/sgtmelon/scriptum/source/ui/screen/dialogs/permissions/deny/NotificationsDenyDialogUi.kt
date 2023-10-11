package sgtmelon.scriptum.source.ui.screen.dialogs.permissions.deny

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.intent.SettingsIntent
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart

/**
 * Class for UI control of [MessageDialog] which open after [Permission.PostNotifications] deny
 * to inform user.
 */
class NotificationsDenyDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_notification_deny,
    R.string.dialog_text_notification_deny
), SettingsIntent {

    override fun positive() = trackIntent { super.positive() }

    override fun onPositiveResult() {
        super.onPositiveResult()
        assertSettingsOpen(context)
    }

    companion object {
        inline operator fun invoke(
            func: NotificationsDenyDialogUi.() -> Unit
        ): NotificationsDenyDialogUi {
            return NotificationsDenyDialogUi().apply { assert() }.apply(func)
        }
    }
}