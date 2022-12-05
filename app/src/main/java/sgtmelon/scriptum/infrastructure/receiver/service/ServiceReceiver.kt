package sgtmelon.scriptum.infrastructure.receiver.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Command
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Values

class ServiceReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        when (intent.getStringExtra(Values.COMMAND)) {
            Command.Eternal.KILL -> callback?.killService()
            Command.Eternal.PING -> callback?.sendPongBroadcast()
        }
    }

    interface Callback {
        fun killService()
        fun sendPongBroadcast()
    }

    companion object {
        operator fun get(callback: Callback): ServiceReceiver {
            return ServiceReceiver().apply { this.callback = callback }
        }
    }
}