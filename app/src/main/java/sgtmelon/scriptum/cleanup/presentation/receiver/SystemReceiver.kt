package sgtmelon.scriptum.cleanup.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar
import sgtmelon.common.utils.getCalendarOrNull
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Eternal
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic

/**
 * Receiver for [SystemLogic] commands.
 */
class SystemReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.System.TIDY_UP_ALARM -> callback?.tidyUpAlarm()
            Command.System.SET_ALARM -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)
                val date = intent.getStringExtra(Eternal.Intent.DATE)
                val showToast = intent.getBooleanExtra(Eternal.Intent.TOAST, Eternal.Default.TOAST)

                val calendar = date?.getCalendarOrNull()

                if (id == Note.Default.ID || calendar == null) return

                callback?.setAlarm(id, calendar, showToast)
            }
            Command.System.CANCEL_ALARM -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

                if (id == Note.Default.ID) return

                callback?.cancelAlarm(id)
            }
            Command.System.NOTIFY_NOTES -> callback?.notifyAllNotes()
            Command.System.CANCEL_NOTE -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

                if (id == Note.Default.ID) return

                callback?.cancelNote(id)
            }
            Command.System.NOTIFY_INFO -> {
                val count = intent.getIntExtra(Eternal.Intent.COUNT, Eternal.Default.COUNT).takeIf {
                    it != Eternal.Default.COUNT
                }

                callback?.notifyCount(count)
            }
            Command.System.CLEAR_BIND -> callback?.clearBind()
            Command.System.CLEAR_ALARM -> callback?.clearAlarm()
        }
    }

    /**
     * Callback which will call after getting [Intent] inside [onReceive].
     */
    interface Callback {

        fun tidyUpAlarm()

        fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean)

        fun cancelAlarm(id: Long)

        fun notifyAllNotes()

        fun cancelNote(id: Long)

        /**
         * If [count] == null it means what need take value from database.
         */
        fun notifyCount(count: Int?)

        fun clearBind()

        fun clearAlarm()
    }

    /**
     * Interface for fast data pass to this class. ALso see [BroadcastControl].
     */
    interface Bridge {

        interface TidyUp {
            fun sendTidyUpAlarmBroadcast()
        }

        interface Alarm {
            fun sendSetAlarmBroadcast(id: Long, calendar: Calendar, showToast: Boolean = true)

            fun sendCancelAlarmBroadcast(item: NoteItem) = sendCancelAlarmBroadcast(item.id)
            fun sendCancelAlarmBroadcast(id: Long)
        }

        interface Bind {
            fun sendNotifyNotesBroadcast()
            fun sendCancelNoteBroadcast(id: Long)

            /**
             * If [count] == null it means what need take value from database.
             */
            fun sendNotifyInfoBroadcast(count: Int? = null)
        }
    }

    companion object {
        operator fun get(callback: Callback): SystemReceiver {
            return SystemReceiver().apply { this.callback = callback }
        }
    }
}