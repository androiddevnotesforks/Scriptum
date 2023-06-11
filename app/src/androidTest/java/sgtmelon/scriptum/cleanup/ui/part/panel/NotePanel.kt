package sgtmelon.scriptum.cleanup.ui.part.panel

import android.view.View
import androidx.annotation.AttrRes
import java.util.Calendar
import org.hamcrest.Matcher
import sgtmelon.extensions.getCalendarText
import sgtmelon.extensions.toText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.ui.dialog.ConvertDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.RankDialogUi
import sgtmelon.scriptum.cleanup.ui.screen.note.INoteAfterConvert
import sgtmelon.scriptum.cleanup.ui.screen.note.INoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.infrastructure.database.DbData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveRank
import sgtmelon.scriptum.infrastructure.utils.extensions.note.isSaveEnabled
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onSave
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.source.ui.action.longClick
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.parts.ContainerPart
import sgtmelon.scriptum.source.ui.parts.UiSubpart
import sgtmelon.scriptum.source.ui.screen.dialogs.ColorDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateTimeCallback
import sgtmelon.scriptum.source.ui.screen.main.BinScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withBackgroundAttr
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withText

/**
 * Part of UI abstraction for [TextNoteScreen] or [RollNoteScreen].
 */
class NotePanel<T : ContainerPart, N : NoteItem>(
    parentContainer: Matcher<View>,
    private val callback: INoteScreen<T, N>
) : UiSubpart(parentContainer),
    DateTimeCallback,
    ConvertDialogUi.Callback,
    ColorDialogUi.Callback,
    RankDialogUi.Callback {

    //region Views

    private val panelContainer = getView(R.id.panel_container)
    private val dividerView = getView(R.id.divider_view)

    private val readContainer = getView(R.id.read_container)
    private val notificationButton = getView(R.id.notification_button)
    private val bindButton = getView(R.id.bind_button)
    private val convertButton = getView(R.id.convert_button)
    private val deleteButton = getView(R.id.delete_button)
    private val editButton = getView(R.id.edit_button)

    private val binContainer = getView(R.id.bin_container)
    private val restoreButton = getView(R.id.restore_button)
    private val restoreOpenButton = getView(R.id.restore_open_button)
    private val clearButton = getView(R.id.clear_button)

    private val editContainer = getView(R.id.edit_container)
    private val undoButton = getView(R.id.undo_button)
    private val redoButton = getView(R.id.redo_button)
    private val rankButton = getView(R.id.rank_button)
    private val colorButton = getView(R.id.color_button)
    private val saveButton = getView(R.id.save_button)

    //endregion

    /**
     * Return user to [BinScreen]
     */
    fun onRestore() = callback.throwOnWrongState(NoteState.BIN) {
        restoreButton.click()
        callback.item.change = getCalendarText()
    }

    fun onRestoreOpen() = apply {
        callback.throwOnWrongState(NoteState.BIN) {
            restoreOpenButton.click()
            callback.apply {
                item.change = getCalendarText()
                state = NoteState.READ
            }.fullAssert()
        }
    }

    /**
     * Return user to [BinScreen]
     */
    fun onClear() = callback.throwOnWrongState(NoteState.BIN) { clearButton.click() }

    /**
     * TODO (add)
     */
    fun onUndo() = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
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
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            redoButton.click()
            callback.apply {
                //            shadowItem = inputControl.redo()
            }.fullAssert()
        }
    }

    fun onRank(rankList: List<RankItem> = listOf(), func: RankDialogUi.() -> Unit = {}) = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            rankButton.click()

            RankDialogUi(func, callback.shadowItem, rankList, callback = this)
        }
    }

    fun onColor(func: ColorDialogUi.() -> Unit = {}) = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            colorButton.click()

            val check = callback.item.color
            ColorDialogUi(func, ColorDialogUi.Place.NOTE, check, callback = this)
        }
    }

    fun onSave() = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            saveButton.click()

            callback.apply {
                state = NoteState.READ

                when (item) {
                    is NoteItem.Text -> applyShadowText().onSave()
                    is NoteItem.Roll -> applyShadowRoll().onSave()
                }

                history.reset()
            }.fullAssert()
        }
    }

    fun onLongSave() = apply {
        callback.throwOnWrongState(NoteState.EDIT, NoteState.NEW) {
            saveButton.longClick(commandAutomator)

            callback.apply {
                state = NoteState.EDIT

                when (item) {
                    is NoteItem.Text -> applyShadowText().onSave()
                    is NoteItem.Roll -> applyShadowRoll().onSave(clearEmpty = false)
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
        callback.throwOnWrongState(NoteState.READ) {
            notificationButton.click()
            DateDialogUi(func, isUpdateDate, callback = this)
        }
    }

    fun onBind() = apply {
        callback.throwOnWrongState(NoteState.READ) {
            bindButton.click()
            with(callback.item) { isStatus = !isStatus }
        }
    }

    fun onConvert(func: ConvertDialogUi.() -> Unit = {}) =
        callback.throwOnWrongState(NoteState.READ) {
            convertButton.click()
            ConvertDialogUi(func, callback.item, callback = this)
        }

    fun onDelete() = callback.throwOnWrongState(NoteState.READ) {
        deleteButton.click()
        callback.item.change = getCalendarText()
    }

    fun onEdit() = apply {
        callback.throwOnWrongState(NoteState.READ) {
            editButton.click()

            it.state = NoteState.EDIT
            it.applyItem()
            it.history.reset()
            it.fullAssert()
        }
    }


    override fun dateResetResult() {
        callback.apply {
            item.alarm.date = DbData.Alarm.Default.DATE
            fullAssert()
        }
    }

    override fun timeSetResult(calendar: Calendar) {
        callback.apply {
            item.alarm.id = 1
            item.alarm.date = calendar.toText()

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
            history.add(HistoryAction.Color(HistoryChange(shadowItem.color, color)))
            shadowItem.color = color

            fullAssert()
        }
    }

    override fun onResultRankDialog(item: RankItem?) {
        callback.apply {
            val idTo = item?.id ?: -1
            val psTo = item?.position ?: -1

            val historyAction = HistoryAction.Rank(
                HistoryChange(shadowItem.rank.id, idTo),
                HistoryChange(shadowItem.rank.position, psTo)
            )
            history.add(historyAction)

            shadowItem.apply {
                rank.id = idTo
                rank.position = psTo
            }

            fullAssert()
        }
    }

    fun assert() {
        callback.apply {
            panelContainer.isDisplayed().withBackgroundAttr(R.attr.clPrimary)

            dividerView.isDisplayed(when (item.type) {
                NoteType.TEXT -> true
                NoteType.ROLL -> state == NoteState.EDIT || state == NoteState.NEW
            }) {
                withSize(heightId = R.dimen.layout_1dp)
            }.withBackgroundAttr(R.attr.clDivider)

            when (state) {
                NoteState.READ -> {
                    readContainer.isDisplayed().withSize(heightId = R.dimen.note_panel_height)
                    binContainer.isDisplayed(value = false)
                    editContainer.isDisplayed(value = false)

                    notificationButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_notifications, getTint(item.haveAlarm))
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
                        .withContentDescription(
                            when (item.type) {
                                NoteType.TEXT -> R.string.description_note_convert_text
                                NoteType.ROLL -> R.string.description_note_convert_roll
                            }
                        )

                    deleteButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_bin, R.attr.clContent)
                        .withContentDescription(R.string.description_note_delete)

                    editButton.withText(R.string.button_note_edit).isDisplayed()
                }
                NoteState.BIN -> {
                    readContainer.isDisplayed(value = false)
                    binContainer.isDisplayed().withSize(heightId = R.dimen.note_panel_height)
                    editContainer.isDisplayed(value = false)

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
                NoteState.EDIT, NoteState.NEW -> {
                    readContainer.isDisplayed(value = false)
                    binContainer.isDisplayed(value = false)
                    editContainer.isDisplayed().withSize(heightId = R.dimen.note_panel_height)

                    val undo = history.available.undo
                    undoButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_undo, getEnableTint(undo))
                        .withContentDescription(R.string.description_note_undo)
                        .isEnabled(undo)

                    val redo = history.available.redo
                    redoButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_redo, getEnableTint(redo))
                        .withContentDescription(R.string.description_note_redo)
                        .isEnabled(redo)

                    rankButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_rank, if (isRankEmpty) {
                            getEnableTint(b = false)
                        } else {
                            getTint(shadowItem.haveRank)
                        })
                        .withContentDescription(R.string.description_note_rank)
                        .isEnabled(!isRankEmpty)

                    colorButton.isDisplayed()
                        .withDrawableAttr(R.drawable.ic_palette, R.attr.clContent)
                        .withContentDescription(R.string.description_note_color)

                    saveButton.withText(R.string.button_note_save).isDisplayed()
                        .isEnabled(shadowItem.isSaveEnabled)
                }
            }
        }
    }

    @AttrRes private fun getTint(b: Boolean) = if (b) R.attr.clAccent else R.attr.clContent

    @AttrRes private fun getEnableTint(b: Boolean) = if (b) R.attr.clContent else R.attr.clDisable

    companion object {
        operator fun <T : ContainerPart, N : NoteItem> invoke(
            parentContainer: Matcher<View>,
            func: NotePanel<T, N>.() -> Unit,
            callback: INoteScreen<T, N>
        ): NotePanel<T, N> {
            return NotePanel(parentContainer, callback).apply(func)
        }
    }
}