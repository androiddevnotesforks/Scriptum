package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.note.NoteActivity
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [NoteActivity], [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
class RollNoteScreen(override var state: State,
        override var noteModel: NoteModel,
        override val isRankEmpty: Boolean
) : ParentUi(), INoteScreen {

    fun assert(func: Assert.() -> Unit) = Assert(callback = this).apply { func() }

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)

    fun enterPanel(func: RollEnterPanel.() -> Unit) = RollEnterPanel.invoke(func, callback = this)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    override var shadowModel = noteModel

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert { onDisplayContent() }
        toolbar { assert { onDisplayContent() } }
        controlPanel { assert { onDisplayContent() } }
        enterPanel { assert { onDisplayContent() } }
    }

    // TODO #TEST возврат данных, контроль выхода с экрана
    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()
    }

    companion object {
        operator fun invoke(func: RollNoteScreen.() -> Unit, state: State,
                            noteModel: NoteModel, isRankEmpty: Boolean = true) =
                RollNoteScreen(state, noteModel, isRankEmpty).apply {
                    fullAssert()
                    func()
                }
    }

    class Assert(private val callback: INoteScreen) : BasicMatch() {

        fun onDisplayContent(): Unit = with(callback) {
            onDisplay(R.id.roll_note_parent_container)
            onDisplay(R.id.roll_note_recycler)
        }

    }

}