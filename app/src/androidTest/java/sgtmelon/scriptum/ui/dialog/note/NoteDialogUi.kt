package sgtmelon.scriptum.ui.dialog.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.ui.ParentUi

class NoteDialogUi : ParentUi() {

    fun assert(func: NoteDialogAssert.() -> Unit) = NoteDialogAssert().apply { func() }

    fun onClickRestore() = action { onClickText(R.string.dialog_menu_restore) }

    fun onClickClear() = action { onClickText(R.string.dialog_menu_clear) }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit) = NoteDialogUi().apply { func() }
    }

}