package sgtmelon.scriptum.cleanup.presentation.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for [NoteActivity] commands.
 */
class NoteScreenReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(Values.COMMAND)) {
            Command.UI.UNBIND_NOTE -> {
                val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

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
        operator fun get(callback: Callback): NoteScreenReceiver {
            return NoteScreenReceiver().apply { this.callback = callback }
        }
    }

}