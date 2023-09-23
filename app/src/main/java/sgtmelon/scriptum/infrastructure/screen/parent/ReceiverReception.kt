package sgtmelon.scriptum.infrastructure.screen.parent

import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import sgtmelon.scriptum.infrastructure.model.key.ReceiverFilter

/**
 * Common interface for register/unregister receivers with style.
 */
interface ReceiverReception {

    val receiverFilter: ReceiverFilter? get() = null
    val receiverList: List<BroadcastReceiver> get() = emptyList()

    fun checkInReceivers(context: Context) {
        val filter = receiverFilter?.value ?: return

        receiverList.forEach {
            ContextCompat.registerReceiver(context, it, filter, RECEIVER_NOT_EXPORTED)
        }
    }

    fun checkOutReceivers(context: Context) {
        if (receiverFilter == null) return

        receiverList.forEach {
            context.unregisterReceiver(it)
        }
    }
}