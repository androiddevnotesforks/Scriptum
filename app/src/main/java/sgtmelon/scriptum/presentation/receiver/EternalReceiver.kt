package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Values
import sgtmelon.scriptum.presentation.service.EternalService

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

    /**
     * Callback which will call after getting [Intent] inside [onReceive].
     */
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