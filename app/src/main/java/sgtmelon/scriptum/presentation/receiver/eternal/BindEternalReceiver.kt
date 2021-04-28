package sgtmelon.scriptum.presentation.receiver.eternal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.domain.model.data.IntentData.Bind
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Receiver for connection between UI and [EternalService]/[BindControl].
 */
class BindEternalReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.Bind.NOTIFY_NOTES -> callback?.notifyAllNotes()
            Command.Bind.CANCEL_NOTE -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

                if (id == Note.Default.ID) return

                callback?.cancelNote(id)
            }
            Command.Bind.NOTIFY_INFO -> {
                val count = intent.getIntExtra(Bind.Intent.COUNT, Bind.Default.COUNT).takeIf {
                    it != Bind.Default.COUNT
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

    interface Broadcast {
        fun sendNotifyNotesBroadcast()
        fun sendCancelNoteBroadcast(id: Long)

        /**
         * If [count] == null it means what need take value from database.
         */
        fun sendNotifyInfoBroadcast(count: Int? = null)
    }

    companion object {
        operator fun get(callback: Callback): BindEternalReceiver {
            return BindEternalReceiver().apply { this.callback = callback }
        }
    }
}