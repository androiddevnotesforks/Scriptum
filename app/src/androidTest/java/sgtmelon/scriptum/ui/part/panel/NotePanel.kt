package sgtmelon.scriptum.ui.part.panel

import androidx.annotation.AttrRes
import sgtmelon.extension.getString
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.ui.dialog.DateDialogUi
import sgtmelon.scriptum.ui.dialog.DateTimeCallback
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import java.util.*

/**
 * Part of UI abstraction for [TextNoteScreen] и [RollNoteScreen]
 */
class NotePanel(private val callback: INoteScreen) : ParentUi(),
        ColorDialogUi.Callback,
        DateTimeCallback {

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

    fun onNotification(updateDate: Boolean = false, func: DateDialogUi.() -> Unit = {}) {
        callback.throwOnWrongState(State.READ) {
            notificationButton.click()
            DateDialogUi.invoke(func, updateDate, callback = this)
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


    override fun onDateDialogResetResult() = with(callback) {
        noteModel.alarmEntity.date = DbData.Alarm.Default.DATE
        fullAssert()
    }

    override fun onTimeDialogResult(calendar: Calendar) = with(callback) {
        noteModel.alarmEntity.date = calendar.getString()
        fullAssert()
    }

    override fun onColorDialogResult(check: Int) = callback.apply {
        inputControl.onColorChange(shadowModel.noteEntity.color, check)
        shadowModel.noteEntity.color = check
    }.fullAssert()


    fun assert() {
        callback.apply {
            parentContainer.isDisplayed()

            when (state) {
                State.READ -> {
                    readContainer.isDisplayed()

                    notificationButton.isDisplayed().withDrawableAttr(
                            R.drawable.ic_notifications,
                            getTint(noteModel.alarmEntity.date.isNotEmpty())
                    )

                    bindButton.isDisplayed().withDrawableAttr(
                            when (noteModel.noteEntity.type) {
                                NoteType.TEXT -> R.drawable.ic_bind_text
                                NoteType.ROLL -> R.drawable.ic_bind_roll
                            }, getTint(noteModel.noteEntity.isStatus)
                    )

                    convertButton.isDisplayed().withDrawableAttr(R.drawable.ic_convert, R.attr.clContent)
                    deleteButton.isDisplayed().withDrawableAttr(R.drawable.ic_bin, R.attr.clContent)

                    editButton.haveText(R.string.menu_note_edit).isDisplayed()
                }
                State.BIN -> {
                    binContainer.isDisplayed()

                    restoreButton.isDisplayed().withDrawableAttr(R.drawable.ic_restore, R.attr.clContent)
                    restoreOpenButton.isDisplayed().withDrawableAttr(R.drawable.ic_restore_open, R.attr.clContent)
                    clearButton.isDisplayed().withDrawableAttr(R.drawable.ic_clear, R.attr.clAccent)
                }
                State.EDIT, State.NEW -> {
                    editContainer.isDisplayed()

                    inputControl.isUndoAccess.let { undo ->
                        undoButton.isDisplayed()
                                .withDrawableAttr(R.drawable.ic_undo, getEnableTint(undo))
                                .isEnabled(undo)
                    }

                    inputControl.isRedoAccess.let { redo ->
                        redoButton.isDisplayed()
                                .withDrawableAttr(R.drawable.ic_redo, getEnableTint(redo))
                                .isEnabled(redo)
                    }

                    rankButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_rank, if (isRankEmpty) {
                                getEnableTint(b = false)
                            } else {
                                getTint(noteModel.noteEntity.haveRank())
                            })
                            .isEnabled(!isRankEmpty)

                    colorButton.isDisplayed().withDrawableAttr(R.drawable.ic_palette, R.attr.clContent)

                    saveButton.haveText(R.string.menu_note_save).isDisplayed().isEnabled(shadowModel.isSaveEnabled())
                }
            }
        }
    }

    @AttrRes private fun getTint(b: Boolean) = if (b) R.attr.clAccent else R.attr.clContent
    @AttrRes private fun getEnableTint(b: Boolean) = if (b) R.attr.clContent else R.attr.clDisable

    companion object {
        operator fun invoke(func: NotePanel.() -> Unit, callback: INoteScreen) =
                NotePanel(callback).apply(func)
    }

}