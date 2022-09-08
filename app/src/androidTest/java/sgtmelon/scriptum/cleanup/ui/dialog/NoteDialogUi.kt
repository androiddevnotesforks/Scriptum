package sgtmelon.scriptum.cleanup.ui.dialog

import java.util.Calendar
import sgtmelon.extensions.toText
import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.time.DateTimeCallback
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [MultipleDialog] when cause long click on note.
 */
class NoteDialogUi(private val item: NoteItem) : ParentUi(), IDialogUi, DateTimeCallback {

    //region Views

    private val titleText = getViewByText(item.name.ifEmpty {
        context.getString(R.string.hint_text_name)
    })

    private val notificationButton = getViewByText(if (item.haveAlarm()) {
        R.string.dialog_menu_notification_update
    } else {
        R.string.dialog_menu_notification_set
    })

    private val bindButton = getViewByText(if (item.isStatus) {
        R.string.dialog_menu_status_unbind
    } else {
        R.string.dialog_menu_status_bind
    })

    private val convertButton = getViewByText(when (item) {
        is NoteItem.Text -> R.string.dialog_menu_convert_text
        is NoteItem.Roll -> R.string.dialog_menu_convert_roll
    })

    private val copyButton = getViewByText(R.string.dialog_menu_copy)

    private val deleteButton = getViewByText(R.string.dialog_menu_delete)
    private val restoreButton = getViewByText(R.string.dialog_menu_restore)
    private val clearButton = getViewByText(R.string.dialog_menu_clear)

    //endregion

    fun onNotification(func: DateDialogUi.() -> Unit) = waitClose {
        notificationButton.click()

        DateDialogUi(func, item.haveAlarm(), callback = this)
    }

    fun onBind() = waitClose {
        bindButton.click()

        item.switchStatus()
    }

    fun onConvert(): NoteItem {
        waitClose { convertButton.click() }

        return when (item) {
            is NoteItem.Text -> item.onConvert()
            is NoteItem.Roll -> item.onConvert()
        }
    }

    fun onCopy() = waitClose { copyButton.click() }

    fun onDelete() = waitClose {
        deleteButton.click()

        item.onDelete()
    }

    fun onRestore() = waitClose {
        restoreButton.click()

        item.onRestore()
    }

    fun onClear() = waitClose { clearButton.click() }


    override fun onDateDialogResetResult() {
        item.alarmDate = DbData.Alarm.Default.DATE
    }

    override fun onTimeDialogResult(calendar: Calendar) {
        item.alarmDate = calendar.toText()
    }

    fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)

        if (item.isBin) {
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
        operator fun invoke(func: NoteDialogUi.() -> Unit, item: NoteItem): NoteDialogUi {
            return NoteDialogUi(item).apply { waitOpen { assert() } }.apply(func)
        }
    }
}