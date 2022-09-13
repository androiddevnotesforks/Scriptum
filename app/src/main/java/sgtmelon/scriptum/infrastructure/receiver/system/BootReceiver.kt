package sgtmelon.scriptum.infrastructure.receiver.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import sgtmelon.scriptum.cleanup.presentation.service.EternalService

/**
 * Receiver, which will looking for a device boot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        /** Skip [EternalService.start] on low API (explanation behind the link). */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EternalService.start(context)
        }
    }
}