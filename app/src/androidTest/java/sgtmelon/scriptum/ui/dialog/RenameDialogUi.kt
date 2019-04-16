package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля диалога [Renamedialog]
 *
 * @author SerjantArbuz
 */
class RenameDialogUi(private val title: String) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(title).apply { func() }

    fun onCloseSoft() = pressBack()

    fun onClickCancel() = action { onClickText(R.string.dialog_btn_cancel) }

    fun onClickAccept() = action { onClickText(R.string.dialog_btn_accept) }

    fun onEnterName(name: String) = action { onEnter(R.id.rename_enter, name) }

    class Assert(private val title: String) : BasicMatch() {

        fun onDisplayContent(enter: String) {
            onDisplayText(title)

            if (enter.isEmpty()) onDisplayHint(R.id.rename_enter, R.string.hint_enter_rank_rename)
            else onDisplay(R.id.rename_enter, enter)

            onDisplayText(R.string.dialog_btn_accept)
            onDisplayText(R.string.dialog_btn_cancel)

            if (enter.isEmpty()) isAcceptEnable(enabled = false)
        }

        fun isAcceptEnable(enabled: Boolean) = isEnabledText(R.string.dialog_btn_accept, enabled)

    }

}