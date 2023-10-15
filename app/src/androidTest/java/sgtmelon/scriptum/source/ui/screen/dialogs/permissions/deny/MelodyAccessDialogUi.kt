package sgtmelon.scriptum.source.ui.screen.dialogs.permissions.deny

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.key.permission.Permission
import sgtmelon.scriptum.source.ui.parts.dialog.permission.PermissionInfoDialogPart
import sgtmelon.scriptum.source.ui.screen.dialogs.permissions.MelodyPermissionDialogUi

/**
 * Class for UI control of [MessageDialog] which open before [Permission.WriteExternalStorage]
 * request. This dialog need to inform user about restrictions (related with melody search feature)
 * if he/she reject permission.
 */
class MelodyAccessDialogUi(
    private val textArray: Array<String>,
    private val initCheck: Int
) : PermissionInfoDialogPart(
    R.string.dialog_title_melody_permission,
    R.string.dialog_text_melody_permission
) {

    override fun positive() {
        super.positive()
        MelodyPermissionDialogUi(textArray, initCheck)
    }

    fun positive(func: MelodyPermissionDialogUi.() -> Unit) {
        super.positive()
        MelodyPermissionDialogUi(textArray, initCheck, func)
    }

    companion object {
        inline operator fun invoke(
            textArray: Array<String>,
            initCheck: Int,
            func: MelodyAccessDialogUi.() -> Unit
        ): MelodyAccessDialogUi {
            return MelodyAccessDialogUi(textArray, initCheck).apply { assert() }.apply(func)
        }
    }
}