package sgtmelon.scriptum.ui.screen.note

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.pressBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.waitBefore

/**
 * Class for UI control of [NoteActivity], [RollNoteFragment]
 */
class RollNoteScreen(
        override var state: State,
        override var noteModel: NoteModel,
        override val isRankEmpty: Boolean
) : ParentRecyclerScreen(R.id.roll_note_recycler), INoteScreen {

    override var shadowModel = NoteModel(noteModel)

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
        enterPanel { assert() }
    }


    fun assert() = Assert()

    fun toolbar(func: NoteToolbar.() -> Unit) = NoteToolbar.invoke(func, callback = this)

    fun enterPanel(func: RollEnterPanel.() -> Unit) = RollEnterPanel.invoke(func, callback = this)

    fun controlPanel(func: NotePanel.() -> Unit) = NotePanel.invoke(func, callback = this)

    fun onSwipeAll() = (0 until count).forEach { onSwipe(it) }

    fun onSwipe(p: Int = positionRandom) = waitBefore(time = 150) {
        action { onSwipeItem(recyclerId, p) }
    }

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


    class Assert : BasicMatch() {
        init {
            onDisplay(R.id.roll_note_parent_container)
            onDisplay(R.id.roll_note_content_container)

            onDisplay(R.id.roll_note_recycler)
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

}