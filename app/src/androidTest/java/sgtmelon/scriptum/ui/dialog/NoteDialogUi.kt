package sgtmelon.scriptum.ui.dialog

import sgtmelon.extension.getString
import sgtmelon.safedialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.extension.getText
import sgtmelon.scriptum.model.data.DbData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.Complete
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.converter.RollConverter
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import java.util.*

/**
 * Class for UI control of [MultipleDialog] when cause long click on note.
 */
class NoteDialogUi(private val noteItem: NoteItem) : ParentUi(), IDialogUi, DateTimeCallback {

    //region Views

    private val notificationButton = getViewByText(if (noteItem.haveAlarm()) {
        R.string.dialog_menu_notification_update
    } else {
        R.string.dialog_menu_notification_set
    })

    private val bindButton = getViewByText(if (noteItem.isStatus) {
        R.string.dialog_menu_status_unbind
    } else {
        R.string.dialog_menu_status_bind
    })

    private val convertButton = getViewByText(when (noteItem.type) {
        NoteType.TEXT -> R.string.dialog_menu_convert_to_roll
        NoteType.ROLL -> R.string.dialog_menu_convert_to_text
    })

    private val copyButton = getViewByText(R.string.dialog_menu_copy)

    private val deleteButton = getViewByText(R.string.dialog_menu_delete)
    private val restoreButton = getViewByText(R.string.dialog_menu_restore)
    private val clearButton = getViewByText(R.string.dialog_menu_clear)

    //endregion

    fun onNotification(func: DateDialogUi.() -> Unit) = waitClose {
        notificationButton.click()

        DateDialogUi.invoke(func, noteItem.haveAlarm(), callback = this)
    }

    fun onBind() = waitClose {
        bindButton.click()

        noteItem.apply { isStatus = !isStatus }
    }

    fun onConvert() = waitClose {
        convertButton.click()

        noteItem.convert()

        /**
         * Type after convert.
         */
        when (noteItem.type) {
            NoteType.ROLL -> {
                val converter = RollConverter()

                noteItem.rollList.clear()

                var p = 0
                noteItem.textToList().forEach {
                    noteItem.rollList.add(converter.toItem(RollEntity().apply {
                        noteId = noteItem.id
                        position = p++
                        text = it
                    }))
                }

                noteItem.updateComplete(Complete.EMPTY)
            }
            NoteType.TEXT -> {
                noteItem.text = noteItem.rollList.getText()
                noteItem.rollList.clear()
            }
        }
    }

    fun onCopy() = waitClose { copyButton.click() }

    fun onDelete() = waitClose {
        deleteButton.click()

        noteItem.delete()
    }

    fun onRestore() = waitClose {
        restoreButton.click()

        noteItem.restore()
    }

    fun onClear() = waitClose { clearButton.click() }


    override fun onDateDialogResetResult() {
        noteItem.alarmDate = DbData.Alarm.Default.DATE
    }

    override fun onTimeDialogResult(calendar: Calendar) {
        noteItem.alarmDate = calendar.getString()
    }

    fun assert() {
        if (noteItem.isBin) {
            restoreButton.isDisplayed().isEnabled()
            copyButton.isDisplayed().isEnabled()
            clearButton.isDisplayed().isEnabled()
        } else {
            bindButton.isDisplayed().isEnabled()
            convertButton.isDisplayed().isEnabled()
            copyButton.isDisplayed().isEnabled()
            deleteButton.isDisplayed().isEnabled()
        }
    }

    companion object {
        operator fun invoke(func: NoteDialogUi.() -> Unit, noteItem: NoteItem) =
                NoteDialogUi(noteItem).apply { waitOpen { assert() } }.apply(func)
    }

}