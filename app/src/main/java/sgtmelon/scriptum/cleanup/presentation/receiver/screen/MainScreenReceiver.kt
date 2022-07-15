package sgtmelon.scriptum.cleanup.presentation.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity

/**
 * Receiver for [MainActivity] commands.
 */
class MainScreenReceiver : BroadcastReceiver() {

    private var bindCallback: BindCallback? = null
    private var alarmCallback: AlarmCallback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(Note.Intent.ID, Note.Default.ID) ?: return

        if (id == Note.Default.ID) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.UI.UNBIND_NOTE -> bindCallback?.onReceiveUnbindNote(id)
            Command.UI.UPDATE_ALARM -> alarmCallback?.onReceiveUpdateAlarm(id)
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
        operator fun get(
            bindCallback: BindCallback,
            alarmCallback: AlarmCallback
        ): MainScreenReceiver {
            return MainScreenReceiver().apply {
                this.bindCallback = bindCallback
                this.alarmCallback = alarmCallback
            }
        }
    }

}