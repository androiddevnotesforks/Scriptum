package sgtmelon.scriptum.source.ui.parts.dialog.permission

import androidx.annotation.StringRes
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.scriptum.source.ui.parts.dialog.MessageDialogPart

/**
 * Parent class for UI control of dialogs with explanation about permission.
 */
abstract class PermissionInfoDialogPart(
    @StringRes titleId: Int,
    @StringRes messageId: Int
) : MessageDialogPart(titleId, messageId, MessageType.Info)