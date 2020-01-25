package sgtmelon.scriptum.ui.part.panel

import androidx.annotation.AttrRes
import sgtmelon.extension.getText
import sgtmelon.extension.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.room.NoteRepo.Companion.onConvertRoll
import sgtmelon.scriptum.repository.room.NoteRepo.Companion.onConvertText
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.ui.dialog.RankDialogUi
import sgtmelon.scriptum.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.ui.dialog.time.DateTimeCallback
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import java.util.*

/**
 * Part of UI abstraction for [TextNoteScreen] и [RollNoteScreen]
 */
class NotePanel<T: ParentUi>(private val callback: INoteScreen<T>) : ParentUi(),
        DateTimeCallback,
        ConvertDialogUi.Callback,
        ColorDialogUi.Callback,
        RankDialogUi.Callback {

    //region Views

    private val parentContainer = getViewById(R.id.note_panel_container)
    private val dividerView = getViewById(R.id.note_panel_divider_view)
    private val buttonContainer = getViewById(R.id.note_panel_button_container)

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
        callback.noteItem.change = getTime()
    }

    fun onRestoreOpen() = apply {
        callback.throwOnWrongState(State.BIN) {
            restoreOpenButton.click()
            callback.apply {
                noteItem.change = getTime()
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
                //            shadowItem = inputControl.undo()
            }.fullAssert()
        }
    }

    fun onRedo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            redoButton.click()
            callback.apply {
                //            shadowItem = inputControl.redo()
            }.fullAssert()
        }
    }

    fun onRank(rankList: List<RankItem> = listOf(), func: RankDialogUi.() -> Unit = {}) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            rankButton.click()

            RankDialogUi.invoke(func, callback.shadowItem, rankList, callback = this)
        }
    }

    fun onColor(func: ColorDialogUi.() -> Unit = {}) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            colorButton.click()

            val check = callback.noteItem.color
            ColorDialogUi.invoke(func, ColorDialogUi.Place.NOTE, check, callback = this)
        }
    }

    fun onSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.click()

            callback.apply {
                state = State.READ

                noteItem = shadowItem.deepCopy()
                noteItem.change = getTime()

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
            with(callback.noteItem) { isStatus = !isStatus }
        }
    }

    fun onConvert(func: ConvertDialogUi.() -> Unit = {}) = callback.throwOnWrongState(State.READ) {
        convertButton.click()
        ConvertDialogUi.invoke(func, callback.noteItem, callback = this)
    }

    fun onDelete() = callback.throwOnWrongState(State.READ) {
        deleteButton.click()
        callback.noteItem.change = getTime()
    }

    fun onEdit() = apply {
        callback.throwOnWrongState(State.READ) {
            editButton.click()

            callback.apply {
                state = State.EDIT
                shadowItem = noteItem.deepCopy()
                inputControl.reset()
            }.fullAssert()
        }
    }


    override fun onDateDialogResetResult() {
        callback.apply {
            noteItem.alarmDate = DbData.Alarm.Default.DATE
            fullAssert()
        }
    }

    override fun onTimeDialogResult(calendar: Calendar) {
        callback.apply {
            noteItem.alarmId = 1
            noteItem.alarmDate = calendar.getText()

            fullAssert()
        }
    }

    override fun onConvertDialogResult() = with(callback) {
        when (shadowItem.type) {
            NoteType.TEXT -> shadowItem.onConvertText()
            NoteType.ROLL -> shadowItem.onConvertRoll()
        }

        noteItem = shadowItem.deepCopy()
    }

    override fun onColorDialogResult(check: Int) {
        callback.apply {
            inputControl.onColorChange(shadowItem.color, check)
            shadowItem.color = check

            fullAssert()
        }
    }

    override fun onResultRankDialog(rankItem: RankItem?) {
        callback.apply {
            val idTo = rankItem?.id ?: -1
            val psTo = rankItem?.position ?: -1

            inputControl.onRankChange(shadowItem.rankId, shadowItem.rankPs, idTo, psTo)
            shadowItem.apply {
                rankId = idTo
                rankPs = psTo
            }

            fullAssert()
        }
    }


    fun assert() {
        callback.apply {
            parentContainer.isDisplayed()

            dividerView.isDisplayed(when(noteItem.type) {
                NoteType.TEXT -> true
                NoteType.ROLL -> state == State.EDIT || state == State.NEW
            }) {
                withSize(heightId = R.dimen.layout_1dp)
            }.withBackgroundAttr(R.attr.clDivider)

            buttonContainer.isDisplayed().withBackgroundAttr(R.attr.clPrimary)
                    .withSize(heightId = R.dimen.note_panel_height)

            when (state) {
                State.READ -> {
                    readContainer.isDisplayed()
                    binContainer.isDisplayed(visible = false)
                    editContainer.isDisplayed(visible = false)

                    notificationButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_notifications, getTint(noteItem.haveAlarm()))
                            .withContentDescription(R.string.description_note_notification)

                    val drawable = when (noteItem.type) {
                        NoteType.TEXT -> R.drawable.ic_bind_text
                        NoteType.ROLL -> R.drawable.ic_bind_roll
                    }

                    bindButton.isDisplayed()
                            .withDrawableAttr(drawable, getTint(noteItem.isStatus))
                            .withContentDescription(if (noteItem.isStatus) {
                                R.string.description_note_unbind
                            } else {
                                R.string.description_note_bind
                            })

                    convertButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_convert, R.attr.clContent)
                            .withContentDescription(when(noteItem.type) {
                                NoteType.TEXT -> R.string.description_note_convert_text
                                NoteType.ROLL -> R.string.description_note_convert_roll
                            })

                    deleteButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_bin, R.attr.clContent)
                            .withContentDescription(R.string.description_note_delete)

                    editButton.withText(R.string.button_note_edit).isDisplayed()
                }
                State.BIN -> {
                    readContainer.isDisplayed(visible = false)
                    binContainer.isDisplayed()
                    editContainer.isDisplayed(visible = false)

                    restoreButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_restore, R.attr.clContent)
                            .withContentDescription(R.string.description_note_restore)

                    restoreOpenButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_restore_open, R.attr.clContent)
                            .withContentDescription(R.string.description_note_restore_open)

                    clearButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_clear, R.attr.clAccent)
                            .withContentDescription(R.string.description_note_clear)
                }
                State.EDIT, State.NEW -> {
                    readContainer.isDisplayed(visible = false)
                    binContainer.isDisplayed(visible = false)
                    editContainer.isDisplayed()

                    val undo = inputControl.isUndoAccess
                    undoButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_undo, getEnableTint(undo))
                            .withContentDescription(R.string.description_note_undo)
                            .isEnabled(undo)

                    val redo = inputControl.isRedoAccess
                    redoButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_redo, getEnableTint(redo))
                            .withContentDescription(R.string.description_note_redo)
                            .isEnabled(redo)

                    rankButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_rank, if (isRankEmpty) {
                                getEnableTint(b = false)
                            } else {
                                getTint(shadowItem.haveRank())
                            })
                            .withContentDescription(R.string.description_note_rank)
                            .isEnabled(!isRankEmpty)

                    colorButton.isDisplayed()
                            .withDrawableAttr(R.drawable.ic_palette, R.attr.clContent)
                            .withContentDescription(R.string.description_note_color)

                    saveButton.withText(R.string.button_note_save).isDisplayed().isEnabled(shadowItem.isSaveEnabled())
                }
            }
        }
    }

    @AttrRes private fun getTint(b: Boolean) = if (b) R.attr.clAccent else R.attr.clContent

    @AttrRes private fun getEnableTint(b: Boolean) = if (b) R.attr.clContent else R.attr.clDisable

    companion object {
        operator fun <T: ParentUi> invoke(func: NotePanel<T>.() -> Unit,
                                          callback: INoteScreen<T>): NotePanel<T> {
            return NotePanel(callback).apply(func)
        }
    }

}