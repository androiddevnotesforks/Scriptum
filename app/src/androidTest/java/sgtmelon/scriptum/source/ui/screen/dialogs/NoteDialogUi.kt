package sgtmelon.scriptum.source.ui.screen.dialogs

import java.util.Calendar
import sgtmelon.extensions.toText
import sgtmelon.safedialog.dialog.OptionsDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.database.DbData
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.note.haveAlarm
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onConvert
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onDelete
import sgtmelon.scriptum.infrastructure.utils.extensions.note.onRestore
import sgtmelon.scriptum.infrastructure.utils.extensions.note.switchStatus
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateTimeCallback
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [OptionsDialog] when cause long click on note.
 */
class NoteDialogUi(val item: NoteItem) : UiPart(),
    DialogUi,
    DateTimeCallback {

    //region Views

    private val titleText = if (item.name.isEmpty()) {
        getViewByText(R.string.empty_note_name)
    } else {
        getViewByText(item.name)
    }

    private val notificationButton = getViewByText(
        if (item.haveAlarm) {
            R.string.dialog_menu_notification_update
        } else {
            R.string.dialog_menu_notification_set
        }
    )

    private val bindButton = getViewByText(
        if (item.isStatus) {
            R.string.dialog_menu_status_unbind
        } else {
            R.string.dialog_menu_status_bind
        }
    )

    private val convertButton = getViewByText(
        when (item.type) {
            NoteType.TEXT -> R.string.dialog_menu_convert_text
            NoteType.ROLL -> R.string.dialog_menu_convert_roll
        }
    )

    private val copyButton = getViewByText(R.string.dialog_menu_copy)

    private val deleteButton = getViewByText(R.string.dialog_menu_delete)
    private val restoreButton = getViewByText(R.string.dialog_menu_restore)
    private val clearButton = getViewByText(R.string.dialog_menu_clear)

    //endregion

    fun notification(func: DateDialogUi.() -> Unit) = waitClose {
        notificationButton.click()
        DateDialogUi(func, item.haveAlarm, callback = this)
    }

    fun bind() = waitClose {
        bindButton.click()
        item.switchStatus()
    }

    fun convert(): NoteItem {
        waitClose { convertButton.click() }
        return when (item) {
            is NoteItem.Text -> item.onConvert()
            is NoteItem.Roll -> item.onConvert()
        }
    }

    fun copy() = waitClose { copyButton.click() }

    fun delete() = waitClose {
        deleteButton.click()
        item.onDelete()
    }

    fun restore() = waitClose {
        restoreButton.click()
        item.onRestore()
    }

    fun clear() = waitClose { clearButton.click() }


    override fun dateResetResult() {
        item.alarm.id = DbData.Alarm.Default.ID
        item.alarm.date = DbData.Alarm.Default.DATE
    }

    override fun timeSetResult(calendar: Calendar) {
        item.alarm.id = calendar.timeInMillis
        item.alarm.date = calendar.toText()
    }

    fun assert() = apply {
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
        inline operator fun invoke(func: NoteDialogUi.() -> Unit, item: NoteItem): NoteDialogUi {
            return NoteDialogUi(item).apply { waitOpen { assert() } }.apply(func)
        }
    }
}