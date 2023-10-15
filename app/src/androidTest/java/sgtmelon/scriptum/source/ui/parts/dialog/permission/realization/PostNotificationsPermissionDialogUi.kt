package sgtmelon.scriptum.source.ui.parts.dialog.permission.realization

import android.Manifest
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionDialogPart

/**
 * Class for UI control of system [Manifest.permission.POST_NOTIFICATIONS] permission dialog.
 */
abstract class PostNotificationsPermissionDialogUi
    : PermissionDialogPart(Permission.PostNotifications)