package sgtmelon.scriptum.infrastructure.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for catch update alarm action and notify screen about it.
 */
class UpdateAlarmReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getStringExtra(Values.COMMAND) != Command.UI.UPDATE_ALARM) return

        val noteId = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)
        if (noteId != Note.Default.ID) {
            callback?.onReceiveUpdateAlarm(noteId)
        }
    }

    /**
     * Interface for update UI elements.
     *
     * Case:
     * - Calls after alarmRepeat for update indicator.
     * - Don't need implement this callback inside rank screen for update icons because
     * application always starts from NOTES page.
     */
    interface Callback {
        fun onReceiveUpdateAlarm(noteId: Long)
    }

    companion object {
        operator fun get(callback: Callback): BroadcastReceiver {
            return UpdateAlarmReceiver().apply { this.callback = callback }
        }
    }
}