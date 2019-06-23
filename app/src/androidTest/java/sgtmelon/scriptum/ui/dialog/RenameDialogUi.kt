package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.RenameDialog
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Класс для ui контроля диалога [RenameDialog]
 *
 * @author SerjantArbuz
 */
class RenameDialogUi(private val title: String) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(title).apply { func() }

    fun onCloseSoft() = waitAfter(time = 300) {
        closeSoftKeyboard()
        pressBack()
    }

    fun onClickCancel() = waitAfter(time = 300) {
        action { onClickText(R.string.dialog_btn_cancel) }
    }

    fun onClickAccept() = waitAfter(time = 300) {
        action { onClickText(R.string.dialog_btn_accept) }
    }

    fun onEnterName(name: String, enabled: Boolean) {
        action { onEnter(R.id.rename_enter, name) }
        assert { onDisplayContent(name, enabled) }
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String) =
                RenameDialogUi(title).apply {
                    waitBefore(time = 100) {
                        assert { onDisplayContent(enter = "", enabled = false) }
                        func()
                    }
                }
    }

    // tODO сделать сразу чек и title и enter
    class Assert(private val title: String) : BasicMatch() {

        fun onDisplayContent(enter: String, enabled: Boolean) {
            onDisplay(R.id.rename_parent_container)
            onDisplayText(title, R.id.rename_parent_container)

            if (enter.isNotEmpty()) {
                onDisplay(R.id.rename_enter, enter)
            } else {
                onDisplayHint(R.id.rename_enter, R.string.hint_enter_rank_rename)
            }

            onDisplayText(R.string.dialog_btn_accept)
            onDisplayText(R.string.dialog_btn_cancel)

            isEnabledText(R.string.dialog_btn_accept, enabled)
        }

    }

}