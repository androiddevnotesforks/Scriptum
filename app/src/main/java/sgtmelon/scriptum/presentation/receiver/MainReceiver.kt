package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity

/**
 * Receiver for [MainActivity] commands.
 */
class MainReceiver() : BroadcastReceiver() {

    private var bindCallback: BindCallback? = null
    private var alarmCallback: AlarmCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(Values.NOTE_ID, NoteData.Default.ID) ?: return

        if (id == NoteData.Default.ID) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.UNBIND_NOTE -> bindCallback?.onReceiveUnbindNote(id)
            Command.UPDATE_ALARM -> alarmCallback?.onReceiveUpdateAlarm(id)
        }
    }

    /**
     * Interface for update UI elements.
     *
     * Calls on note notification cancel from status bar for update bind indicator.
     */
    interface BindCallback {
        fun onReceiveUnbindNote(id: Long)
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
        operator fun get(bindCallback: BindCallback, alarmCallback: AlarmCallback): MainReceiver {
            return MainReceiver().apply {
                this.bindCallback = bindCallback
                this.alarmCallback = alarmCallback
            }
        }
    }

}