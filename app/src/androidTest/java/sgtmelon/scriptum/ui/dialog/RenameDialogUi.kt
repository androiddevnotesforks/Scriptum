package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.RenameDialog
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Class for UI control of [RenameDialog]
 */
class RenameDialogUi(private val title: String) : ParentDialogUi() {

    fun assert(enter: String = "", enabled: Boolean = false) = Assert(title, enter, enabled)


    fun onRename(name: String, enabled: Boolean) = apply {
        action { onEnter(R.id.rename_enter, name) }
        assert(name, enabled)
    }

    fun onClickCancel() = waitClose { action { onClickText(R.string.dialog_button_cancel) } }

    fun onClickAccept() = waitClose { action { onClickText(R.string.dialog_button_accept) } }


    class Assert(title: String, enter: String, enabled: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.rename_parent_container)
            onDisplayText(title, R.id.rename_parent_container)

            if (enter.isNotEmpty()) {
                onDisplay(R.id.rename_enter, enter)
            } else {
                onDisplayHint(R.id.rename_enter, R.string.hint_enter_rank_rename)
            }

            onDisplayText(R.string.dialog_button_cancel)
            onDisplayText(R.string.dialog_button_accept)

            isEnabledText(R.string.dialog_button_accept, enabled)
        }
    }

    companion object {
        operator fun invoke(func: RenameDialogUi.() -> Unit, title: String) =
                RenameDialogUi(title).apply { waitOpen { assert() } }.apply(func)
    }

}