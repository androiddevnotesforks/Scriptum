package sgtmelon.scriptum.ui.part.panel

import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Part of UI abstraction for [TextNoteScreen] и [RollNoteScreen]
 */
class NotePanel(private val callback: INoteScreen) : ParentUi(), ColorDialogUi.Callback {

    //region Views

    private val parentContainer = getViewById(R.id.note_panel_container)

    private val readContainer = getViewById(R.id.note_panel_read_container)
    private val notificationButton = getViewById(R.id.note_panel_notification_button)
    private val bindButton = getViewById(R.id.note_panel_bind_button)
    private val convertButton = getViewById(R.id.note_panel_convert_button)
    private val deleteButton = getViewById(R.id.note_panel_delete_button)
    private val editButton = getViewById(R.id.note_panel_edit_button)

    private val binContainer = getViewById(R.id.note_panel_bin_container)
    private val restoreButton = getViewById(R.id.note_panel_restore_button)
    private val restoreOpenButton = getViewById(R.id.note_panel_restore_open_button)
    private val clearButton = getViewById(R.id.note_panel_clear_button)

    private val editContainer = getViewById(R.id.note_panel_edit_container)
    private val undoButton = getViewById(R.id.note_panel_undo_button)
    private val redoButton = getViewById(R.id.note_panel_redo_button)
    private val rankButton = getViewById(R.id.note_panel_rank_button)
    private val colorButton = getViewById(R.id.note_panel_color_button)
    private val saveButton = getViewById(R.id.note_panel_save_button)

    //endregion

    /**
     * Return user to [BinScreen]
     */
    fun onRestore() = callback.throwOnWrongState(State.BIN) {
        restoreButton.click()
        callback.noteModel.noteEntity.change = getTime()
    }

    fun onRestoreOpen() = apply {
        callback.throwOnWrongState(State.BIN) {
            restoreOpenButton.click()
            callback.apply {
                noteModel.noteEntity.change = getTime()
                state = State.READ
            }.fullAssert()
        }
    }

    /**
     * Return user to [BinScreen]
     */
    fun onClear() = callback.throwOnWrongState(State.BIN) { clearButton.click() }

    fun onUndo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            undoButton.click()
            callback.apply {
                //            shadowModel = inputControl.undo()
            }.fullAssert()
        }
    }

    fun onRedo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            redoButton.click()
            callback.apply {
                //            shadowModel = inputControl.redo()
            }.fullAssert()
        }
    }

    fun onRank() = callback.throwOnWrongState(State.EDIT, State.NEW) { rankButton.click() }

    fun onColor(func: ColorDialogUi.() -> Unit = {}) {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            colorButton.click()

            val check = callback.noteModel.noteEntity.color
            ColorDialogUi.invoke(func, ColorDialogUi.Place.NOTE, check, callback = this)
        }
    }

    fun onSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.click()

            callback.apply {
                state = State.READ

                noteModel = NoteModel(shadowModel)
                noteModel.noteEntity.change = getTime()

                inputControl.reset()
            }.fullAssert()
        }
    }

    fun onBind() = apply {
        callback.throwOnWrongState(State.READ) {
            bindButton.click()
            with(callback.noteModel.noteEntity) { isStatus = !isStatus }
        }
    }

    fun onConvert(func: ConvertDialogUi.() -> Unit = {}) = callback.throwOnWrongState(State.READ) {
        convertButton.click()
        ConvertDialogUi.invoke(func, callback.noteModel)
    }

    fun onDelete() = callback.throwOnWrongState(State.READ) {
        deleteButton.click()
        callback.noteModel.noteEntity.change = getTime()
    }

    fun onEdit() = apply {
        callback.throwOnWrongState(State.READ) {
            editButton.click()

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


    fun assert() {
        callback.apply {
            parentContainer.isDisplayed()

            when (state) {
                State.READ -> {
                    readContainer.isDisplayed()

                    notificationButton.isDisplayed()
                    bindButton.isDisplayed()
                    convertButton.isDisplayed()
                    deleteButton.isDisplayed()

                    editButton.isDisplayed()
                }
                State.BIN -> {
                    binContainer.isDisplayed()

                    restoreButton.isDisplayed()
                    restoreOpenButton.isDisplayed()
                    clearButton.isDisplayed()
                }
                State.EDIT, State.NEW -> {
                    editContainer.isDisplayed()

                    undoButton.isDisplayed().isEnabled(inputControl.isUndoAccess)
                    redoButton.isDisplayed().isEnabled(inputControl.isRedoAccess)
                    rankButton.isDisplayed().isEnabled(!isRankEmpty)
                    colorButton.isDisplayed()

                    saveButton.isDisplayed().isEnabled(shadowModel.isSaveEnabled())
                }
            }
        }
    }

    companion object {
        operator fun invoke(func: NotePanel.() -> Unit, callback: INoteScreen) =
                NotePanel(callback).apply(func)
    }

}