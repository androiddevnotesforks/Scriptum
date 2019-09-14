package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.safedialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Класс для ui контроля диалога [MessageDialog] при отчистке корзины
 */
class ClearDialogUi : ParentUi() {

    fun assert() = Assert()

    fun onCloseSoft() = waitAfter(time = 300) { pressBack() }

    fun onClickNo() = waitAfter(time = 300) { action { onClickText(R.string.dialog_button_no) } }

    fun onClickYes() = waitAfter(time = 300) { action { onClickText(R.string.dialog_button_yes) } }

    companion object {
        operator fun invoke(func: ClearDialogUi.() -> Unit) = ClearDialogUi().apply {
            waitBefore(time = 100) {
                assert()
                func()
            }
        }
    }

    class Assert : BasicMatch() {
        init {
            onDisplayText(R.string.dialog_title_clear_bin)
            onDisplayText(R.string.dialog_text_clear_bin)

            onDisplayText(R.string.dialog_button_yes)
            onDisplayText(R.string.dialog_button_no)
        }
    }

}