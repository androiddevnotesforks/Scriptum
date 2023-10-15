package sgtmelon.scriptum.source.ui.screen.dialogs.permissions

import sgtmelon.scriptum.source.ui.parts.dialog.permission.realization.WriteExternalPermissionDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.select.MelodyDialogUi

/**
 * Realization of write external storage permission dialog with ability to check result.
 */
class MelodyPermissionDialogUi(
    private val textArray: Array<String>,
    private val initCheck: Int
) : WriteExternalPermissionDialogUi() {

    override fun allow() {
        super.allow()
        MelodyDialogUi(textArray, initCheck)
    }

    fun allow(func: MelodyDialogUi.() -> Unit) {
        super.allow()
        MelodyDialogUi(textArray, initCheck, func)
    }

    override fun deny() {
        super.deny()
        MelodyDialogUi(textArray, initCheck)
    }

    fun deny(func: MelodyDialogUi.() -> Unit) {
        super.deny()
        MelodyDialogUi(textArray, initCheck, func)
    }

    override fun notAsk() {
        super.notAsk()
        MelodyDialogUi(textArray, initCheck)
    }

    fun notAsk(func: MelodyDialogUi.() -> Unit) {
        super.notAsk()
        MelodyDialogUi(textArray, initCheck, func)
    }

    companion object {
        inline operator fun invoke(
            textArray: Array<String>,
            initCheck: Int,
            func: MelodyPermissionDialogUi.() -> Unit
        ): MelodyPermissionDialogUi {
            return MelodyPermissionDialogUi(textArray, initCheck).apply { assert() }.apply(func)
        }
    }
}