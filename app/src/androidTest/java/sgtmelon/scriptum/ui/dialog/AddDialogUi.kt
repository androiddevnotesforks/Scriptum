package sgtmelon.scriptum.ui.dialog

import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.dialog.SheetAddDialog
import sgtmelon.scriptum.model.key.NoteType
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

    fun textNoteScreen(func: TextNoteScreen.() -> Unit = {}) = TextNoteScreen().apply {
        onClickItem(NoteType.TEXT)
        assert { onDisplayContent(State.NEW) }
        func()
    }

    fun rollNoteScreen(func: RollNoteScreen.() -> Unit = {}) = RollNoteScreen().apply {
        onClickItem(NoteType.ROLL)
        assert { onDisplayContent(State.NEW) }
        func()
    }

    fun onCloseSoft() = pressBack()

    fun onCloseSwipe() = action {
        onSwipeDown(R.id.add_navigation)
        wait(time = 500)
    }

    private fun onClickItem(type: NoteType) = action {
        onClickText(when (type) {
            NoteType.TEXT -> R.string.dialog_add_text
            NoteType.ROLL -> R.string.dialog_add_roll
        })
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.add_navigation)
            onDisplayText(R.string.dialog_add_text)
            onDisplayText(R.string.dialog_add_roll)
        }

    }

}