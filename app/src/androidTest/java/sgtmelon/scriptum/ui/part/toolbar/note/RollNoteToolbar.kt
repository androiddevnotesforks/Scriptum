package sgtmelon.scriptum.ui.part.toolbar.note

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen

/**
 * Realisation a part of UI abstraction for [RollNoteScreen].
 */
class RollNoteToolbar<T : ParentUi>(
        callback: INoteScreen<T, NoteItem.Roll>,
        imeCallback: ImeCallback
) : ParentNoteToolbar<T, NoteItem.Roll>(callback, imeCallback) {

    override fun onClickBack() {
        super.onClickBack()

        with(callback) {
            if (state == State.EDIT) {
                state = State.READ
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
                fullAssert()
            }
        }
    }

    companion object {
        operator fun <T : ParentUi> invoke(func: RollNoteToolbar<T>.() -> Unit,
                                           callback: INoteScreen<T, NoteItem.Roll>,
                                           imeCallback: ImeCallback): RollNoteToolbar<T> {
            return RollNoteToolbar(callback, imeCallback).apply { assert() }.apply(func)
        }
    }

}