package sgtmelon.scriptum.ui.dialog.note

import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi

class NoteDialogUi : ParentUi() {

    fun assert(func: NoteDialogAssert.() -> Unit) = NoteDialogAssert().apply { func() }

    fun onClickItem(type: NoteType) = action {}

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit) = NoteDialogUi().apply { func() }
    }

}