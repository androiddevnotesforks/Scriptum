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

    fun assert(enter: String = "", enabled: Boolean = false) = Assert(title, enter, enabled)

    fun onCloseSoft() = waitAfter(time = 300) {
        closeSoftKeyboard()
        pressBack()
    }

    fun onClickCancel() = waitAfter(time = 300) {
        action { onClickText(R.string.dialog_button_cancel) }
    }

    fun onClickAccept() = waitAfter(time = 300) {
        action { onClickText(R.string.dialog_button_accept) }
    }

    fun onEnterName(name: String, enabled: Boolean) {
        action { onEnter(R.id.rename_enter, name) }
        assert(name, enabled)
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String) =
                RenameDialogUi(title).apply {
                    waitBefore(time = 100) {
                        assert()
                        func()
                    }
                }
    }

    class Assert(title: String, enter: String, enabled: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.rename_parent_container)
            onDisplayText(title, R.id.rename_parent_container)

            if (enter.isNotEmpty()) {
                onDisplay(R.id.rename_enter, enter)
            } else {
                onDisplayHint(R.id.rename_enter, R.string.hint_enter_rank_rename)
            }

            onDisplayText(R.string.dialog_button_accept)
            onDisplayText(R.string.dialog_button_cancel)

            isEnabledText(R.string.dialog_button_accept, enabled)
        }
    }

}