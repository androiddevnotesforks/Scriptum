package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.content.Intent
import java.util.Calendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter

/**
 * Class, which delegate send of broadcast messages.
 */
class BroadcastDelegator(private val context: Context) {

    /** Function for let UI know about note unbind action. */
    fun sendUnbindNoteUi(noteId: Long) {
        val places = listOf(Filter.RANK, Filter.NOTES, Filter.NOTE, Filter.ALARM)

        context.sendTo(places, Command.UI.UNBIND_NOTE) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    //region Bind functions

    fun sendNotifyNotesBind() = context.sendTo(Filter.SYSTEM, Command.System.NOTIFY_NOTES)

    fun sendCancelNoteBind(item: NoteItem) = sendCancelNoteBind(item.id)

    fun sendCancelNoteBind(noteId: Long) {
        context.sendTo(Filter.SYSTEM, Command.System.CANCEL_NOTE) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    /**
     * If [count] == null it means: "we don't know exactly, please, take correct value from DB".
     */
    fun sendNotifyInfoBind(count: Int? = null) {
        context.sendTo(Filter.SYSTEM, Command.System.NOTIFY_INFO) {
            if (count != null) {
                putExtra(IntentData.Eternal.Key.COUNT, count)
            }
        }
    }

    fun sendClearBind() = context.sendTo(Filter.SYSTEM, Command.System.CLEAR_BIND)

    //endregion

    //region Alarm functions

    fun sendTidyUpAlarm() = context.sendTo(Filter.SYSTEM, Command.System.TIDY_UP_ALARM)

    fun sendSetAlarm(item: NoteItem, calendar: Calendar, showToast: Boolean = true) =
        sendSetAlarm(item.id, calendar, showToast)

    fun sendSetAlarm(noteId: Long, calendar: Calendar, showToast: Boolean) {
        context.sendTo(Filter.SYSTEM, Command.System.SET_ALARM) {
            putExtra(IntentData.Note.Key.ID, noteId)
            putExtra(IntentData.Eternal.Key.DATE, calendar.toText())
            putExtra(IntentData.Eternal.Key.TOAST, showToast)
        }
    }

    fun sendCancelAlarm(item: NotificationItem) = sendCancelAlarm(item.note.id)

    fun sendCancelAlarm(item: NoteItem) = sendCancelAlarm(item.id)

    fun sendCancelAlarm(noteId: Long) {
        context.sendTo(Filter.SYSTEM, Command.System.CANCEL_ALARM) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    fun sendClearAlarm() = context.sendTo(Filter.SYSTEM, Command.System.CLEAR_ALARM)

    //endregion

    //region Eternal functions

    fun sendEternalKill() = context.sendTo(Filter.ETERNAL, Command.Eternal.KILL)

    fun sendEternalPing() = context.sendTo(Filter.ETERNAL, Command.Eternal.PING)

    fun sendEternalPong() = context.sendTo(Filter.DEVELOP, Command.Eternal.PONG)

    //endregion

    private inline fun Context.sendTo(
        place: String,
        command: String,
        extras: Intent.() -> Unit = {}
    ) {
        sendBroadcast(Intent(place).apply {
            putExtra(ReceiverData.Values.COMMAND, command)
            putExtras(Intent().apply(extras))
        })
    }

    private inline fun Context.sendTo(
        places: List<String>,
        command: String,
        extras: Intent.() -> Unit = {}
    ) {
        for (place in places) {
            sendTo(place, command, extras)
        }
    }
}