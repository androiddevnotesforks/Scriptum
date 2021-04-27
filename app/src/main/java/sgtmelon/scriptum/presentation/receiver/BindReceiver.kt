package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Receiver for connection between UI and [EternalService]/[BindControl].
 */
class BindReceiver : BroadcastReceiver() {

    private var callback: Callback? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        //        TODO("Not yet implemented")
    }

    interface Callback {
        fun notifyNoteList()
        fun cancelNote(id: Long)
        fun notifyInfo(count: Int)
    }

    companion object {
        operator fun get(callback: Callback): BindReceiver {
            return BindReceiver().apply { this.callback = callback }
        }
    }
}