package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control [SheetAddDialog]
 */
class AddDialogUi : ParentDialogUi() {

    fun assert() = Assert()


    fun createTextNote(noteModel: NoteModel, func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(NoteType.TEXT)
        TextNoteScreen.invoke(func, State.NEW, noteModel)
    }

    fun createRollNote(noteModel: NoteModel, func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(NoteType.ROLL)
        RollNoteScreen.invoke(func, State.NEW, noteModel)
    }

    private fun onClickItem(type: NoteType) = action {
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    fun onCloseSwipe() = waitClose { action { onSwipeDown(R.id.add_navigation) } }


    class Assert : BasicMatch() {
        init {
            onDisplay(R.id.add_navigation)
            onDisplayText(R.string.dialog_add_text)
            onDisplayText(R.string.dialog_add_roll)
        }
    }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) =
                AddDialogUi().apply(func).apply { waitOpen { assert() } }
    }

}