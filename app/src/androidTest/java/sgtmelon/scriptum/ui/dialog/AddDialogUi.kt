package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.ui.ParentUi

class AddDialogUi : ParentUi() {

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) = AddDialogUi().apply { func() }
    }

    fun assert(func: AddDialogAssert.() -> Unit) = AddDialogAssert().apply { func() }

    fun onClickItem(type: NoteType) = action {
        onClick(when (type) {
            NoteType.TEXT -> R.id.item_add_note
            NoteType.ROLL -> R.id.item_add_roll
        })
    }

}