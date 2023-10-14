package sgtmelon.scriptum.source.ui.screen.dialogs.permissions.deny

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart
import sgtmelon.scriptum.source.ui.screen.dialogs.permissions.WriteExternalPermissionDialogUi

/**
 * Class for UI control of [MessageDialog] which open before [Permission.WriteExternalStorage]
 * request. This dialog need to inform user about restrictions (related with melody search feature)
 * if he/she reject permission.
 */
class MelodyAccessDialogUi : PermissionInfoDialogPart(
    R.string.dialog_title_melody_permission,
    R.string.dialog_text_melody_permission
) {

    // TODO add on result callback

    override fun positive() {
        super.positive()
        WriteExternalPermissionDialogUi.invoke {}
    }

    fun positive(func: WriteExternalPermissionDialogUi.() -> Unit) {
        super.positive()
        WriteExternalPermissionDialogUi(func)
    }

    companion object {
        inline operator fun invoke(
            func: MelodyAccessDialogUi.() -> Unit
        ): MelodyAccessDialogUi {
            return MelodyAccessDialogUi().apply { assert() }.apply(func)
        }
    }
}