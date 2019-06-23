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

    override var shadowModel = NoteModel(noteModel)

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert { onDisplayContent() }
        toolbar { assert { onDisplayContent() } }
        controlPanel { assert { onDisplayContent() } }
        enterPanel { assert { onDisplayContent() } }
    }

    fun assert(func: Assert.() -> Unit) = Assert(callback = this).apply { func() }
    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)
    fun enterPanel(func: RollEnterPanel.() -> Unit) = RollEnterPanel.invoke(func, callback = this)
    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    fun onPressBack() {
        closeSoftKeyboard()
        pressBack()

        if (state == State.EDIT || state == State.NEW) {
            if (shadowModel.isSaveEnabled()) {
                state = State.READ
                noteModel = NoteModel(shadowModel)
                inputControl.reset()
                fullAssert()
            } else if (state == State.EDIT) {
                state = State.READ
                shadowModel = NoteModel(noteModel)
                inputControl.reset()
                fullAssert()
            }
        }
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