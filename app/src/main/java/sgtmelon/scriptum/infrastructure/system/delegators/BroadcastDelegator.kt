package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.content.Intent
import sgtmelon.extensions.encode
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import java.util.Calendar

/**
 * Class, which delegate send of broadcast messages.
 */
class BroadcastDelegator(private val context: Context) {

    /** Function for let UI know about note unbind action. */
    fun sendUnbindNoteUi(noteId: Long) {
        val places = listOf(ReceiverFilter.RANK, ReceiverFilter.NOTES, ReceiverFilter.NOTE, ReceiverFilter.ALARM)

        context.sendTo(places, Command.UI.UNBIND_NOTE) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    fun sendInfoChangeUi(state: ShowListState, place: ReceiverFilter) {
        context.sendTo(place, Command.UI.INFO_CHANGE) {
            putExtra(IntentData.ShowList.Key.VALUE, state.encode())
        }
    }

    fun sendInfoChangeUi(changeId: Long, place: ReceiverFilter) {
        context.sendTo(place, Command.UI.INFO_CHANGE) {
            putExtra(IntentData.ShowList.Key.ID, changeId)
        }
    }

    //region Bind functions

    fun sendNotifyNotesBind() = context.sendTo(ReceiverFilter.ETERNAL, Command.System.NOTIFY_NOTES)

    fun sendCancelNoteBind(item: NoteItem) = sendCancelNoteBind(item.id)

    fun sendCancelNoteBind(noteId: Long) {
        context.sendTo(ReceiverFilter.ETERNAL, Command.System.CANCEL_NOTE) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    /**
     * If [count] == null it means: "we don't know exactly, please, take correct value from DB".
     */
    fun sendNotifyInfoBind(count: Int? = null) {
        context.sendTo(ReceiverFilter.ETERNAL, Command.System.NOTIFY_INFO) {
            if (count != null) {
                putExtra(IntentData.Eternal.Key.COUNT, count)
            }
        }
    }

    fun sendClearBind() = context.sendTo(ReceiverFilter.ETERNAL, Command.System.CLEAR_BIND)

    //endregion

    //region Alarm functions

    fun sendTidyUpAlarm() = context.sendTo(ReceiverFilter.ETERNAL, Command.System.TIDY_UP_ALARM)

    fun sendSetAlarm(item: NoteItem, calendar: Calendar, showToast: Boolean = true) =
        sendSetAlarm(item.id, calendar, showToast)

    fun sendSetAlarm(noteId: Long, calendar: Calendar, showToast: Boolean) {
        context.sendTo(ReceiverFilter.ETERNAL, Command.System.SET_ALARM) {
            putExtra(IntentData.Note.Key.ID, noteId)
            putExtra(IntentData.Eternal.Key.DATE, calendar.toText())
            putExtra(IntentData.Eternal.Key.TOAST, showToast)
        }
    }

    fun sendCancelAlarm(item: NotificationItem) = sendCancelAlarm(item.note.id)

    fun sendCancelAlarm(item: NoteItem) = sendCancelAlarm(item.id)

    private fun sendCancelAlarm(noteId: Long) {
        context.sendTo(ReceiverFilter.ETERNAL, Command.System.CANCEL_ALARM) {
            putExtra(IntentData.Note.Key.ID, noteId)
        }
    }

    fun sendClearAlarm() = context.sendTo(ReceiverFilter.ETERNAL, Command.System.CLEAR_ALARM)

    //endregion

    //region Develop functions

    fun sendEternalKill() = context.sendTo(ReceiverFilter.ETERNAL, Command.Develop.KILL)

    fun sendEternalPing() = context.sendTo(ReceiverFilter.ETERNAL, Command.Develop.PING)

    fun sendEternalPong() = context.sendTo(ReceiverFilter.DEVELOP, Command.Develop.PONG)

    //endregion

    private inline fun Context.sendTo(
        place: ReceiverFilter,
        command: String,
        extras: Intent.() -> Unit = {}
    ) {
        sendBroadcast(Intent(place.action).apply {
            putExtra(ReceiverData.Values.COMMAND, command)
            putExtras(Intent().apply(extras))
        })
    }

    private inline fun Context.sendTo(
        places: List<ReceiverFilter>,
        command: String,
        extras: Intent.() -> Unit = {}
    ) = places.forEach { sendTo(it, command, extras) }

}