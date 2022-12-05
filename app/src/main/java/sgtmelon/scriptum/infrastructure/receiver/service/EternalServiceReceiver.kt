package sgtmelon.scriptum.infrastructure.receiver.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar
import sgtmelon.extensions.toCalendarOrNull
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Eternal
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values
import sgtmelon.scriptum.infrastructure.service.EternalServiceLogicImpl
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator

/**
 * Receiver for [EternalServiceLogicImpl] commands.
 */
class EternalServiceReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.System.TIDY_UP_ALARM -> callback?.tidyUpAlarm()
            Command.System.SET_ALARM -> setAlarm(intent)
            Command.System.CANCEL_ALARM -> cancelAlarm(intent)
            Command.System.NOTIFY_NOTES -> callback?.notifyAllNotes()
            Command.System.CANCEL_NOTE -> cancelNote(intent)
            Command.System.NOTIFY_INFO -> notifyInfo(intent)
            Command.System.CLEAR_BIND -> callback?.clearBind()
            Command.System.CLEAR_ALARM -> callback?.clearAlarm()
        }
    }

    private fun setAlarm(intent: Intent) {
        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)
        val date = intent.getStringExtra(Eternal.Intent.DATE)
        val showToast = intent.getBooleanExtra(Eternal.Intent.TOAST, Eternal.Default.TOAST)

        val calendar = date?.toCalendarOrNull()

        if (id == Note.Default.ID || calendar == null) return

        callback?.setAlarm(id, calendar, showToast)
    }

    private fun cancelAlarm(intent: Intent) {
        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

        if (id == Note.Default.ID) return

        callback?.cancelAlarm(id)
    }

    private fun cancelNote(intent: Intent) {
        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

        if (id == Note.Default.ID) return

        callback?.cancelNote(id)
    }

    private fun notifyInfo(intent: Intent) {
        val count = intent.getIntExtra(Eternal.Intent.COUNT, Eternal.Default.COUNT)

        if (count == Eternal.Default.COUNT) return

        callback?.notifyCount(count)
    }

    /**
     * Callback, which will be called after getting an [Intent] inside [onReceive] function.
     */
    interface Callback {

        fun tidyUpAlarm()

        fun setAlarm(noteId: Long, calendar: Calendar, showToast: Boolean)

        fun cancelAlarm(noteId: Long)

        fun notifyAllNotes()

        fun cancelNote(noteId: Long)

        /**
         * If [count] == null it means what need take value from database.
         */
        fun notifyCount(count: Int?)

        fun clearBind()

        fun clearAlarm()
    }

    /**
     * Interface for fast data pass to this class. ALso see [BroadcastDelegator].
     */
    @Deprecated("use delegators class, remove calls from vm")
    interface Bridge {

        interface Alarm {
            fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean = true)
            fun sendCancelAlarmBroadcast(id: Long)

            fun sendCancelAlarmBroadcast(item: NoteItem) = sendCancelAlarmBroadcast(item.id)
            fun sendCancelAlarmBroadcast(item: NotificationItem) =
                sendCancelAlarmBroadcast(item.note.id)
        }

        interface Bind {
            fun sendNotifyNotesBroadcast()

            fun sendCancelNoteBroadcast(item: NoteItem) = sendCancelNoteBroadcast(item.id)
            fun sendCancelNoteBroadcast(id: Long)

            /**
             * If [count] == null it means what need take value from database.
             */
            fun sendNotifyInfoBroadcast(count: Int? = null)
        }
    }

    companion object {
        operator fun get(callback: Callback): EternalServiceReceiver {
            return EternalServiceReceiver().apply { this.callback = callback }
        }
    }
}