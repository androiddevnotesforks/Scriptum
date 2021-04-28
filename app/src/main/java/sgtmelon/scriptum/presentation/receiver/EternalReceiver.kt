package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.domain.model.data.IntentData.Eternal
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.service.EternalService
import java.util.*

/**
 * Receiver for connection between UI and [EternalService]/[BindControl].
 */
class EternalReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.Eternal.NOTIFY_NOTES -> callback?.notifyAllNotes()
            Command.Eternal.CANCEL_NOTE -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

                if (id == Note.Default.ID) return

                callback?.cancelNote(id)
            }
            Command.Eternal.NOTIFY_INFO -> {
                val count = intent.getIntExtra(Eternal.Intent.COUNT, Eternal.Default.COUNT).takeIf {
                    it != Eternal.Default.COUNT
                }

                callback?.notifyInfo(count)
            }
        }
    }

    interface Callback {
        fun notifyAllNotes()
        fun cancelNote(id: Long)

        /**
         * If [count] == null it means what need take value from database.
         */
        fun notifyInfo(count: Int?)
    }

    interface Bridge {

        interface Bind {
            fun sendNotifyNotesBroadcast()
            fun sendCancelNoteBroadcast(id: Long)

            /**
             * If [count] == null it means what need take value from database.
             */
            fun sendNotifyInfoBroadcast(count: Int? = null)
        }

        interface Alarm {
            fun sendSetAlarmBroadcast(calendar: Calendar, id: Long, showToast: Boolean = true)
            fun sendCancelAlarmBroadcast(id: Long)
        }
    }

    companion object {
        operator fun get(callback: Callback): EternalReceiver {
            return EternalReceiver().apply { this.callback = callback }
        }
    }
}