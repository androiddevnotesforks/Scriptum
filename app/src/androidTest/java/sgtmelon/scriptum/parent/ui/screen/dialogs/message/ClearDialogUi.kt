package sgtmelon.scriptum.parent.ui.screen.dialogs.message

import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.parts.dialog.MessageDialogPart

/**
 * Class for UI control of [MessageDialog] when clear bin.
 */
class ClearDialogUi : MessageDialogPart(
    R.string.dialog_title_clear_bin,
    R.string.dialog_text_clear_bin,
    sgtmelon.safedialog.R.string.dialog_button_no,
    sgtmelon.safedialog.R.string.dialog_button_yes
) {

    companion object {
        inline operator fun invoke(func: ClearDialogUi.() -> Unit): ClearDialogUi {
            return ClearDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}