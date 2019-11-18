package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.swipeItem
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.part.panel.NotePanel
import sgtmelon.scriptum.ui.part.panel.RollEnterPanel
import sgtmelon.scriptum.ui.part.toolbar.NoteToolbar

/**
 * Class for UI control of [NoteActivity], [RollNoteFragment].
 */
class RollNoteScreen(
        override var state: State,
        override var noteItem: NoteItem,
        override val isRankEmpty: Boolean
) : ParentRecyclerScreen(R.id.roll_note_recycler), INoteScreen, IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.roll_note_parent_container)
    private val contentContainer = getViewById(R.id.roll_note_content_container)

    //endregion

    override var shadowItem: NoteItem = noteItem.deepCopy()

    override val inputControl = InputControl().apply { isEnabled = true }

    override fun fullAssert() {
        assert()
        toolbar { assert() }
        controlPanel { assert() }
        enterPanel { assert() }
    }

    fun toolbar(func: NoteToolbar.() -> Unit) = apply {
        NoteToolbar.invoke(func, callback = this)
    }

    fun enterPanel(func: RollEnterPanel.() -> Unit) = apply {
        RollEnterPanel.invoke(func, callback = this)
    }

    fun controlPanel(func: NotePanel.() -> Unit) = apply {
        NotePanel.invoke(func, callback = this)
    }

    fun onSwipeAll() = repeat(times = count) { onSwipe() }

    fun onSwipe(p: Int = random) {
        waitAfter(SWIPE_TIME) { recyclerView.swipeItem(p) }
        assert()
    }

    override fun onPressBack() {
        super.onPressBack()

        if (state == State.EDIT || state == State.NEW) {
            if (shadowItem.isSaveEnabled()) {
                state = State.READ
                noteItem = shadowItem.deepCopy()
                inputControl.reset()
                fullAssert()
            } else if (state == State.EDIT) {
                state = State.READ
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }


    fun assert() {
        parentContainer.isDisplayed()
        contentContainer.isDisplayed()

        recyclerView.isDisplayed()
    }

    companion object {
        private const val SWIPE_TIME = 150L

        operator fun invoke(func: RollNoteScreen.() -> Unit, state: State,
                            noteItem: NoteItem, isRankEmpty: Boolean = true): RollNoteScreen {
            if (noteItem.type != NoteType.ROLL) {
                throw IllegalAccessException("Wrong note type!")
            }

            return RollNoteScreen(state, noteItem, isRankEmpty).apply { fullAssert() }.apply(func)
        }
    }

}