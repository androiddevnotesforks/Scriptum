package sgtmelon.scriptum.ui.dialog.clear

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.basic.BasicMatch

class ClearDialogAssert: BasicMatch() {

    fun onDisplayContent() {
        onDisplayText(R.string.dialog_title_clear_bin)
        onDisplayText(R.string.dialog_text_clear_bin)

        onDisplayText(R.string.dialog_btn_yes)
        onDisplayText(R.string.dialog_btn_no)
    }

}