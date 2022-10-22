package sgtmelon.scriptum.infrastructure.receiver.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.presentation.service.EternalService
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

/**
 * Receiver for [EternalService] commands.
 */
class EternalReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.Eternal.KILL -> callback?.killService()
            Command.Eternal.PING -> callback?.sendEternalPongBroadcast()
        }
    }

    interface Callback {
        fun killService()
        fun sendEternalPongBroadcast()
    }

    companion object {
        operator fun get(callback: Callback): EternalReceiver {
            return EternalReceiver().apply { this.callback = callback }
        }
    }
}