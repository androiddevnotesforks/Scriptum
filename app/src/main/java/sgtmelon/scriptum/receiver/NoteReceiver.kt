package sgtmelon.scriptum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.key.ReceiverKey.Command
import sgtmelon.scriptum.model.key.ReceiverKey.Values
import sgtmelon.scriptum.screen.view.note.NoteActivity

/**
 * Ресивер обработки комманд для [NoteActivity]
 *
 * @author SerjantArbuz
 */
class NoteReceiver(private val callback: Callback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> {
                val id = intent.getLongExtra(Values.NOTE_ID, Values.ID_UNDEFINED)

                if (id != Values.ID_UNDEFINED) callback.onReceiveUnbindNote(id)
            }
        }
    }

    interface Callback {
        fun onReceiveUnbindNote(id: Long)
    }

}