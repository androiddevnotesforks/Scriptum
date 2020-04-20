package sgtmelon.scriptum.ui.part.toolbar.note

import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Realisation a part of UI abstraction for [TextNoteScreen].
 */
class TextNoteToolbar<T : ParentUi>(
        callback: INoteScreen<T, NoteItem.Text>,
        imeCallback: ImeCallback
) : ParentNoteToolbar<T, NoteItem.Text>(callback, imeCallback) {

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
        operator fun <T: ParentUi> invoke(func: TextNoteToolbar<T>.() -> Unit,
                                          callback: INoteScreen<T, NoteItem.Text>,
                                          imeCallback: ImeCallback): TextNoteToolbar<T> {
            return TextNoteToolbar(callback, imeCallback).apply { assert() }.apply(func)
        }
    }

}