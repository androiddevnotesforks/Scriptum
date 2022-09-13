package sgtmelon.scriptum.cleanup.presentation.receiver.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for developer commands.
 */
class DevelopScreenReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.Eternal.PONG -> callback?.onReceiveEternalServicePong()
        }
    }

    /** Callback, which will be called after getting an [Intent] inside [onReceive] function. */
    interface Callback {
        fun onReceiveEternalServicePong()
    }


    companion object {
        operator fun get(callback: Callback): DevelopScreenReceiver {
            return DevelopScreenReceiver().apply { this.callback = callback }
        }
    }
}