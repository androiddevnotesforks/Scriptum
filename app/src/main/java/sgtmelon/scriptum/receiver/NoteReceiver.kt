package sgtmelon.scriptum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.screen.ui.note.NoteActivity

/**
 * Ресивер обработки комманд для [NoteActivity]
 */
class NoteReceiver(private val callback: Callback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> {
                val id = intent.getLongExtra(Values.NOTE_ID, Values.ND_NOTE_ID)

                if (id != Values.ND_NOTE_ID) callback.onReceiveUnbindNote(id)
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