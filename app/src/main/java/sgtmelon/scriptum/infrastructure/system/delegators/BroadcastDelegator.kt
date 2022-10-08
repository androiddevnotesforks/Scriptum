package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import java.util.Calendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.cleanup.extension.sendTo
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter

/**
 * Class, which delegate send of broadcast messages.
 */
class BroadcastDelegator(private val context: Context) {

    /** Function for let UI know about note unbind action. */
    fun sendUnbindNoteUi(noteId: Long) {
        val places = listOf(Filter.MAIN, Filter.NOTE, Filter.ALARM)

        context.sendTo(places, Command.UI.UNBIND_NOTE) {
            putExtra(IntentData.Note.Intent.ID, noteId)
        }
    }

    /** Function for let UI know about some alarm changes. */
    fun sendUpdateAlarmUi(noteId: Long) {
        context.sendTo(Filter.MAIN, Command.UI.UPDATE_ALARM) {
            putExtra(IntentData.Note.Intent.ID, noteId)
        }
    }

    //region Bind functions

    fun sendNotifyNotesBind() = context.sendTo(Filter.SYSTEM, Command.System.NOTIFY_NOTES)

    fun sendCancelNoteBind(noteId: Long) {
        context.sendTo(Filter.SYSTEM, Command.System.CANCEL_NOTE) {
            putExtra(IntentData.Note.Intent.ID, noteId)
        }
    }

    fun sendNotifyInfoBind(count: Int?) {
        context.sendTo(Filter.SYSTEM, Command.System.NOTIFY_INFO) {
            if (count != null) {
                putExtra(IntentData.Eternal.Intent.COUNT, count)
            }
        }
    }

    fun sendClearBind() = context.sendTo(Filter.SYSTEM, Command.System.CLEAR_BIND)

    //endregion

    //region Alarm functions

    fun sendTidyUpAlarm() = context.sendTo(Filter.SYSTEM, Command.System.TIDY_UP_ALARM)

    fun sendSetAlarm(noteId: Long, calendar: Calendar, showToast: Boolean) {
        context.sendTo(Filter.SYSTEM, Command.System.SET_ALARM) {
            putExtra(IntentData.Note.Intent.ID, noteId)
            putExtra(IntentData.Eternal.Intent.DATE, calendar.toText())
            putExtra(IntentData.Eternal.Intent.TOAST, showToast)
        }
    }

    fun sendCancelAlarm(noteId: Long) {
        context.sendTo(Filter.SYSTEM, Command.System.CANCEL_ALARM) {
            putExtra(IntentData.Note.Intent.ID, noteId)
        }
    }

    fun sendClearAlarm() = context.sendTo(Filter.SYSTEM, Command.System.CLEAR_ALARM)

    //endregion

    //region Eternal functions

    fun sendEternalKill() = context.sendTo(Filter.ETERNAL, Command.Eternal.KILL)

    fun sendEternalPing() = context.sendTo(Filter.ETERNAL, Command.Eternal.PING)

    fun sendEternalPong() = context.sendTo(Filter.DEVELOP, Command.Eternal.PONG)

    //endregion
}