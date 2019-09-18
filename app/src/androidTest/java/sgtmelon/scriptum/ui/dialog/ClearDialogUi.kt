package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Class for UI control of [MessageDialog] when clear bin
 */
class ClearDialogUi : ParentDialogUi() {

    fun assert() = Assert()


    fun onClickNo() = waitClose { action { onClickText(R.string.dialog_button_no) } }

    fun onClickYes() = waitClose { action { onClickText(R.string.dialog_button_yes) } }


    class Assert : BasicMatch() {
        init {
            onDisplayText(R.string.dialog_title_clear_bin)
            onDisplayText(R.string.dialog_text_clear_bin)

            onDisplayText(R.string.dialog_button_yes)
            onDisplayText(R.string.dialog_button_no)
        }
    }

    companion object {
        operator fun invoke(func: ClearDialogUi.() -> Unit) = ClearDialogUi().apply(func)
                .apply { waitOpen { assert() } }
    }

}