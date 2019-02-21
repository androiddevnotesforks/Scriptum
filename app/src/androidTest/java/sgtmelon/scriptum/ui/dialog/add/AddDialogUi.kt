package sgtmelon.scriptum.ui.dialog.add

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.ui.ParentUi

class AddDialogUi : ParentUi() {

    fun assert(func: AddDialogAssert.() -> Unit) = AddDialogAssert().apply { func() }

    fun onClickItem(type: NoteType) = action {
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    fun open() = action { onClick(R.id.main_add_fab) }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) = AddDialogUi().apply { func() }
    }

}