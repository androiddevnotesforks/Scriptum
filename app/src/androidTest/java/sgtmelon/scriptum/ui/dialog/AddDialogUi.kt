package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля диалога [SheetAddDialog]
 *
 * @author SerjantArbuz
 */
class AddDialogUi : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun createTextNote(func: TextNoteScreen.() -> Unit = {}){
        onClickItem(NoteType.TEXT)
        TextNoteScreen.invoke(func, State.NEW, NoteEntity())
    }

    fun createRollNote(func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(NoteType.ROLL)
        RollNoteScreen.invoke(func, State.NEW, NoteEntity())
    }

    fun onCloseSoft() = pressBack()

    fun onCloseSwipe() = action {
        onSwipeDown(R.id.add_navigation)
        waitBefore(time = 500)
    }

    private fun onClickItem(type: NoteType) = action {
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    companion object {
        operator fun invoke(func: AddDialogUi.() -> Unit) = AddDialogUi().apply {
            assert { onDisplayContent() }
            func()
        }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.add_navigation)
            onDisplayText(R.string.dialog_add_text)
            onDisplayText(R.string.dialog_add_roll)
        }

    }

}