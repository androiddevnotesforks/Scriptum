package sgtmelon.scriptum.ui.dialog.note

import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.ui.ParentUi

class NoteDialogUi : ParentUi() {

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit) = NoteDialogUi().apply { func() }
    }

    fun assert(func: NoteDialogAssert.() -> Unit) = NoteDialogAssert().apply { func() }

    fun onClickItem(type: NoteType) = action {}

}