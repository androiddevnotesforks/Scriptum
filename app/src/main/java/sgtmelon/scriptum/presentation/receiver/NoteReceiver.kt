package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity

/**
 * Receiver for [NoteActivity] commands
 */
class NoteReceiver(private val callback: Callback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> {
                val id = intent.getLongExtra(Values.NOTE_ID, NoteData.Default.ID)

                if (id != NoteData.Default.ID) callback.onReceiveUnbindNote(id)
            }
        }
    }

    /**
     * Interface for update UI elements
     */
    interface Callback {
        fun onReceiveUnbindNote(id: Long)
    }

}