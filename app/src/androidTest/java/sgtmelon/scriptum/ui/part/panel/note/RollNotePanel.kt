package sgtmelon.scriptum.ui.part.panel.note

import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen

/**
 * Realisation a part of UI abstraction for [RollNoteScreen].
 */
class RollNotePanel<T : ParentUi>(
        callback: INoteScreen<T, NoteItem.Roll>
) : ParentNotePanel<T, NoteItem.Roll>(callback) {

    override fun onSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.click()

            callback.apply {
                state = State.READ

                noteItem = shadowItem.deepCopy()
                noteItem.onSave()

                inputControl.reset()
            }.fullAssert()
        }
    }

    override fun onLongSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.longClick()

            callback.apply {
                state = State.EDIT

                noteItem = shadowItem.deepCopy()
                noteItem.onSave()
            }.fullAssert()
        }
    }

    override fun onEdit() = apply {
        callback.throwOnWrongState(State.READ) {
            editButton.click()

            callback.apply {
                state = State.EDIT
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
            }.fullAssert()
        }
    }

    // TODO fix
    override fun onConvertDialogResult() = with(callback) {
        shadowItem.onConvert()
        noteItem = shadowItem.deepCopy()
    }

    companion object {
        operator fun <T : ParentUi> invoke(func: RollNotePanel<T>.() -> Unit,
                                           callback: INoteScreen<T, NoteItem.Roll>): RollNotePanel<T> {
            return RollNotePanel(callback).apply(func)
        }
    }

}