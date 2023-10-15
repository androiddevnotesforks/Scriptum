package sgtmelon.scriptum.source.ui.parts.dialog.permission.realization

import android.Manifest
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionDialogPart

/**
 * Class for UI control of system [Manifest.permission.WRITE_EXTERNAL_STORAGE] permission dialog.
 */
abstract class WriteExternalPermissionDialogUi
    : PermissionDialogPart(Permission.WriteExternalStorage)