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
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    fun open() = action { onClick(R.id.add_fab) }

}