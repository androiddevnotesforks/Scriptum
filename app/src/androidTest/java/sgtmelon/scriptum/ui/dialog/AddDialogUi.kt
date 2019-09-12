package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Класс для ui контроля диалога [SheetAddDialog]
 *
 * @author SerjantArbuz
 */
class AddDialogUi : ParentUi() {

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

    fun onCloseSoft() = waitAfter(time = 300) { pressBack() }

    fun onCloseSwipe() = waitAfter(time = 300) { action { onSwipeDown(R.id.add_navigation) } }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) = AddDialogUi().apply {
            waitBefore(time = 100) {
                assert()
                func()
            }
        }
    }

    class Assert : BasicMatch() {
        init {
            onDisplay(R.id.add_navigation)
            onDisplayText(R.string.dialog_add_text)
            onDisplayText(R.string.dialog_add_roll)
        }
    }

}