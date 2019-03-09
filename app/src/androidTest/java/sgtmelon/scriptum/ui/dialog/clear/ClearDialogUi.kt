package sgtmelon.scriptum.ui.dialog.clear

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi

class ClearDialogUi : ParentUi() {

    fun assert(func: ClearDialogAssert.() -> Unit) = ClearDialogAssert().apply { func() }

    fun onClickNo() = action { onClickText(R.string.dialog_btn_no) }

    fun onClickYes() = action { onClickText(R.string.dialog_btn_yes) }

    companion object {
        operator fun invoke(func: ClearDialogUi.() -> Unit) = ClearDialogUi().apply { func() }
    }

}