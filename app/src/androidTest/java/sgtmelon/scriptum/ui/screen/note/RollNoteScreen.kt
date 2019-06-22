package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.widget.NotePanel
import sgtmelon.scriptum.ui.widget.NoteToolbar
import sgtmelon.scriptum.ui.widget.RollEnterPanel

/**
 * Класс для ui контроля экрана [NoteActivity], [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollNoteScreen : ParentUi() {

    // TODO !! сделать передачу состояния заметки, и автоматические assert при вызове

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func)

    fun enterPanel(func: RollEnterPanel.() -> Unit) = RollEnterPanel.invoke(func)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func)

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(func: RollNoteScreen.() -> Unit, state: State, noteModel: NoteModel) =
                RollNoteScreen().apply {
                    assert { onDisplayContent(state) }
                    controlPanel { assert { onDisplayContent(state) } }
                    func()
                }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            onDisplay(R.id.roll_note_parent_container)
            onDisplay(R.id.roll_note_recycler)

            RollEnterPanel { assert { onDisplayContent(state) } }
        }

    }

}