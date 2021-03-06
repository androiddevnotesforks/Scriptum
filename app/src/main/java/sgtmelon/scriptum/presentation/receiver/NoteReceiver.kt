package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity

/**
 * Receiver for [NoteActivity] commands.
 */
class NoteReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> {
                val id = intent.getLongExtra(Values.NOTE_ID, Note.Default.ID)

                if (id != Note.Default.ID) callback?.onReceiveUnbindNote(id)
            }
        }
    }

    /**
     * Interface for update UI elements
     */
    interface Callback {
        fun onReceiveUnbindNote(id: Long)
    }

    companion object {
        operator fun get(callback: Callback): NoteReceiver {
            return NoteReceiver().apply { this.callback = callback }
        }
    }

}