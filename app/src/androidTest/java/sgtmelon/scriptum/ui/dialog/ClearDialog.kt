package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class ClearDialog : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onClickNo() = action { onClickText(R.string.dialog_btn_no) }

    fun onClickYes() = action { onClickText(R.string.dialog_btn_yes) }

    companion object {
        operator fun invoke(func: ClearDialog.() -> Unit) = ClearDialog().apply { func() }
    }

    class Assert: BasicMatch() {

        fun onDisplayContent() {
            onDisplayText(R.string.dialog_title_clear_bin)
            onDisplayText(R.string.dialog_text_clear_bin)

            onDisplayText(R.string.dialog_btn_yes)
            onDisplayText(R.string.dialog_btn_no)
        }

    }

}