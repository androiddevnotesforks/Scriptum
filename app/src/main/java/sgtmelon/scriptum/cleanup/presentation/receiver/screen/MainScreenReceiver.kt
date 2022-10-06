package sgtmelon.scriptum.cleanup.presentation.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for [MainActivity] commands.
 */
class MainScreenReceiver : BroadcastReceiver() {

    private var alarmCallback: AlarmCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(Note.Intent.ID, Note.Default.ID) ?: return

        if (id == Note.Default.ID) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.UI.UPDATE_ALARM -> alarmCallback?.onReceiveUpdateAlarm(id)
        }
    }

    /**
     * Interface for update UI elements.
     *
     * Calls after alarmRepeat for update indicator.
     *
     * Don't need implement this callback inside rank screen for update icons because
     * application always starts from NOTES page.
     */
    interface AlarmCallback {
        fun onReceiveUpdateAlarm(id: Long)
    }

    companion object {
        operator fun get(
            alarmCallback: AlarmCallback
        ): MainScreenReceiver {
            return MainScreenReceiver().apply {
                this.alarmCallback = alarmCallback
            }
        }
    }

}