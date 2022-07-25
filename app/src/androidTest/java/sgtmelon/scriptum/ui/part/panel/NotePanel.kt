package sgtmelon.scriptum.ui.part.panel

import androidx.annotation.AttrRes
import java.util.Calendar
import sgtmelon.common.utils.getText
import sgtmelon.common.utils.getTime
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.longClick
import sgtmelon.scriptum.basic.extension.withBackgroundAttr
import sgtmelon.scriptum.basic.extension.withContentDescription
import sgtmelon.scriptum.basic.extension.withDrawableAttr
import sgtmelon.scriptum.basic.extension.withSize
import sgtmelon.scriptum.basic.extension.withText
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.dialog.ColorDialogUi
import sgtmelon.scriptum.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.ui.dialog.RankDialogUi
import sgtmelon.scriptum.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.ui.dialog.time.DateTimeCallback
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.note.INoteAfterConvert
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Part of UI abstraction for [TextNoteScreen] or [RollNoteScreen].
 */

class NotePanel<T : ParentUi, N : NoteItem>(
    private val callback: INoteScreen<T, N>
) : ParentUi(),
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
        callback.item.change = getTime()
    }

    fun onRestoreOpen() = apply {
        callback.throwOnWrongState(State.BIN) {
            restoreOpenButton.click()
            callback.apply {
                item.change = getTime()
                state = State.READ
            }.fullAssert()
        }
    }

    /**
     * Return user to [BinScreen]
     */
    fun onClear() = callback.throwOnWrongState(State.BIN) { clearButton.click() }

    /**
     * TODO (add)
     */
    fun onUndo() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            undoButton.click()
            callback.apply {
                //            shadowItem = inputControl.undo()
            }.fullAssert()
        }
    }

    /**
     * TODO (add)
     */
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

            RankDialogUi(func, callback.shadowItem, rankList, callback = this)
        }
    }

    fun onColor(func: ColorDialogUi.() -> Unit = {}) = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            colorButton.click()

            val check = callback.item.color
            ColorDialogUi(func, ColorDialogUi.Place.NOTE, check, callback = this)
        }
    }

    fun onSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.click()

            callback.apply {
                state = State.READ

                when (item) {
                    is NoteItem.Text -> applyShadowText().onSave()
                    is NoteItem.Roll -> applyShadowRoll().onSave()
                }

                inputControl.reset()
            }.fullAssert()
        }
    }

    fun onLongSave() = apply {
        callback.throwOnWrongState(State.EDIT, State.NEW) {
            saveButton.longClick()

            callback.apply {
                state = State.EDIT

                when (item) {
                    is NoteItem.Text -> applyShadowText().onSave()
                    is NoteItem.Roll -> applyShadowRoll().onSave()
                }
                /**
                 * Need apply [NoteItem.Text.onSave]/[NoteItem.Roll.onSave] for
                 * [INoteScreen.shadowItem] because [INoteScreen.state] not changed.
                 */
                applyItem()
            }.fullAssert()
        }
    }

    fun onNotification(isUpdateDate: Boolean = false, func: DateDialogUi.() -> Unit = {}) {
        callback.throwOnWrongState(State.READ) {
            notificationButton.click()
            DateDialogUi(func, isUpdateDate, callback = this)
        }
    }

    fun onBind() = apply {
        callback.throwOnWrongState(State.READ) {
            bindButton.click()
            with(callback.item) { isStatus = !isStatus }
        }
    }

    fun onConvert(func: ConvertDialogUi.() -> Unit = {}) = callback.throwOnWrongState(State.READ) {
        convertButton.click()
        ConvertDialogUi(func, callback.item, callback = this)
    }

    fun onDelete() = callback.throwOnWrongState(State.READ) {
        deleteButton.click()
        callback.item.change = getTime()
    }

    fun onEdit() = apply {
        callback.throwOnWrongState(State.READ) {
            editButton.click()

            it.state = State.EDIT
            it.applyItem()
            it.inputControl.reset()
            it.fullAssert()
        }
    }


    override fun onDateDialogResetResult() {
        callback.apply {
            item.alarmDate = DbData.Alarm.Default.DATE
            fullAssert()
        }
    }

    override fun onTimeDialogResult(calendar: Calendar) {
        callback.apply {
            item.alarmId = 1
            item.alarmDate = calendar.getText()

            fullAssert()
        }
    }

    /**
     * [NoteItem.Text.onConvert]/[NoteItem.Roll.onConvert] happen inside [INoteAfterConvert]
     */
    override fun onConvertDialogResult() {
        when (callback.item) {
            is NoteItem.Text -> callback.applyShadowText()
            is NoteItem.Roll -> callback.applyShadowRoll()
        }
    }

    override fun onColorDialogResult(color: Color) {
        callback.apply {
            inputControl.onColorChange(shadowItem.color, color)
            shadowItem.color = color

            fullAssert()
        }
    }

    override fun onResultRankDialog(item: RankItem?) {
        callback.apply {
            val idTo = item?.id ?: -1
            val psTo = item?.position ?: -1

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

            dividerView.isDisplayed(when (item.type) {
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
                    binContainer.isDisplayed(isVisible = false)
                    editContainer.isDisplayed(isVisible = false)

                    notificationButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_notifications, getTint(item.haveAlarm()))
                        .withContentDescription(R.string.description_note_notification)

                    val drawable = when (item.type) {
                        NoteType.TEXT -> R.drawable.ic_bind_text
                        NoteType.ROLL -> R.drawable.ic_bind_roll
                    }

                    bindButton.isDisplayed()
                        .withDrawableAttr(drawable, getTint(item.isStatus))
                        .withContentDescription(if (item.isStatus) {
                            R.string.description_note_unbind
                        } else {
                            R.string.description_note_bind
                        })

                    convertButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_convert, R.attr.clContent)
                        .withContentDescription(when (item.type) {
                            NoteType.TEXT -> R.string.description_note_convert_text
                            NoteType.ROLL -> R.string.description_note_convert_roll
                        })

                    deleteButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_bin, R.attr.clContent)
                        .withContentDescription(R.string.description_note_delete)

                    editButton.withText(R.string.button_note_edit).isDisplayed()
                }
                State.BIN -> {
                    readContainer.isDisplayed(isVisible = false)
                    binContainer.isDisplayed()
                    editContainer.isDisplayed(isVisible = false)

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
                    readContainer.isDisplayed(isVisible = false)
                    binContainer.isDisplayed(isVisible = false)
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

                    saveButton.withText(R.string.button_note_save).isDisplayed()
                        .isEnabled(shadowItem.isSaveEnabled())
                }
            }
        }
    }

    @AttrRes private fun getTint(b: Boolean) = if (b) R.attr.clAccent else R.attr.clContent

    @AttrRes private fun getEnableTint(b: Boolean) = if (b) R.attr.clContent else R.attr.clDisable

    companion object {
        operator fun <T : ParentUi, N : NoteItem> invoke(
            func: NotePanel<T, N>.() -> Unit,
            callback: INoteScreen<T, N>
        ): NotePanel<T, N> {
            return NotePanel(callback).apply(func)
        }
    }
}