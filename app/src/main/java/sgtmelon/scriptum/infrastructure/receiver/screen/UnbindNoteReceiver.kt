package sgtmelon.scriptum.infrastructure.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for catch unbind note action and notify screen about it.
 */
class UnbindNoteReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getStringExtra(Values.COMMAND) != Command.UI.UNBIND_NOTE) return

        val noteId = intent.getLongExtra(IntentData.Note.Intent.ID, IntentData.Note.Default.ID)
        if (noteId != IntentData.Note.Default.ID) {
            callback?.onReceiveUnbindNote(noteId)
        }
    }

    /**
     * Interface for update UI elements
     */
    interface Callback {
        fun onReceiveUnbindNote(noteId: Long)
    }

    companion object {
        operator fun get(callback: Callback): BroadcastReceiver {
            return UnbindNoteReceiver().apply { this.callback = callback }
        }
    }
}