package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.screen.ui.main.MainActivity

/**
 * Receiver for [MainActivity] commands
 */
class MainReceiver(private val callback: Callback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(Values.NOTE_ID, NoteData.Default.ID) ?: return

        if (id == NoteData.Default.ID) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> callback.onReceiveUnbindNote(id)
            Command.UPDATE_ALARM -> callback.onReceiveUpdateAlarm(id)
        }
    }

    /**
     * Interface for update UI elements
     */
    interface Callback {
        fun onReceiveUnbindNote(id: Long)
        fun onReceiveUpdateAlarm(id: Long)
    }

}