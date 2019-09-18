package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.ui.screen.main.BinScreen

/**
 * Часть UI абстракции для [TextNoteScreen] и [RollNoteScreen]
 */
class NotePanel(private val callback: INoteScreen) : ParentUi(), ColorDialogUi.Callback {

    fun assert() = Assert(callback)

    /**
     * Return user to [BinScreen]
     */
    fun onRestore() = callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_restore_button) }
    }

    fun onRestoreOpen() = apply { callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_restore_open_button) }
        callback.apply { state = State.READ }.fullAssert()
    } }

    /**
     * Return user to [BinScreen]
     */
    fun onClear() = callback.throwOnWrongState(State.BIN) {
        action { onClick(R.id.note_panel_clear_button) }
    }

    fun onUndo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            action { onClick(R.id.note_panel_undo_button) }
            callback.apply {
                //            shadowModel = inputControl.undo()
            }.fullAssert()
        }
    }

    fun onRedo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            action { onClick(R.id.note_panel_redo_button) }
            callback.apply {
                //            shadowModel = inputControl.redo()
            }.fullAssert()
        }
    }

    fun onRank() = callback.throwOnWrongState(State.EDIT, State.NEW) {
        action { onClick(R.id.note_panel_rank_button) }
    }

    fun onColor(func: ColorDialogUi.() -> Unit = {}) {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            action { onClick(R.id.note_panel_color_button) }

            val check = callback.noteModel.noteEntity.color
            ColorDialogUi.invoke(func, ColorDialogUi.Place.NOTE, check, callback = this)
        }
    }

    fun onSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
        action { onClick(R.id.note_panel_save_button) }

        callback.apply {
            state = State.READ
            noteModel = NoteModel(shadowModel)
            inputControl.reset()
        }.fullAssert()
        }
    }

    fun onBind() = apply {
        callback.throwOnWrongState(State.READ) {
        action { onClick(R.id.note_panel_bind_button) }
        with(callback.noteModel.noteEntity) { isStatus = !isStatus }
        }
    }

    fun onConvert(func: ConvertDialogUi.() -> Unit = {}) = callback.throwOnWrongState(State.READ) {
        action { onClick(R.id.note_panel_convert_button) }
        ConvertDialogUi.invoke(func, callback.noteModel)
    }

    fun onDelete() = callback.throwOnWrongState(State.READ) {
        action { onClick(R.id.note_panel_delete_button) }
    }

    fun onEdit() = apply {
        callback.throwOnWrongState(State.READ) {
            action { onClick(R.id.note_panel_edit_button) }

            callback.apply {
                state = State.EDIT
                shadowModel = NoteModel(noteModel)
                inputControl.reset()
            }.fullAssert()
        }
    }

    override fun onDialogResult(check: Int) = callback.apply {
        inputControl.onColorChange(shadowModel.noteEntity.color, check)
        shadowModel.noteEntity.color = check
    }.fullAssert()


    class Assert(callback: INoteScreen) : BasicMatch() {
        init {
            callback.apply {
                onDisplay(R.id.note_panel_container)

                when (state) {
                    State.READ -> {
                        onDisplay(R.id.note_panel_read_container)

                        onDisplay(R.id.note_panel_notification)
                        onDisplay(R.id.note_panel_bind_button)
                        onDisplay(R.id.note_panel_convert_button)
                        onDisplay(R.id.note_panel_delete_button)

                        onDisplay(R.id.note_panel_edit_button)
                    }
                    State.BIN -> {
                        onDisplay(R.id.note_panel_bin_container)

                        onDisplay(R.id.note_panel_restore_button)
                        onDisplay(R.id.note_panel_restore_open_button)
                        onDisplay(R.id.note_panel_clear_button)
                    }
                    State.EDIT, State.NEW -> {
                        onDisplay(R.id.note_panel_edit_container)

                        onDisplay(R.id.note_panel_undo_button)
                        onDisplay(R.id.note_panel_redo_button)
                        onDisplay(R.id.note_panel_rank_button)
                        onDisplay(R.id.note_panel_color_button)

                        onDisplay(R.id.note_panel_save_button)

                        isEnabled(R.id.note_panel_undo_button, inputControl.isUndoAccess)
                        isEnabled(R.id.note_panel_redo_button, inputControl.isRedoAccess)
                        isEnabled(R.id.note_panel_save_button, shadowModel.isSaveEnabled())
                        isEnabled(R.id.note_panel_rank_button, !isRankEmpty)
                    }
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: NotePanel.() -> Unit, callback: INoteScreen) =
                NotePanel(callback).apply(func)
    }

}