package sgtmelon.scriptum.infrastructure.receiver.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.presentation.service.EternalService

/**
 * Receiver, which will looking for a device boot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        /** Restart our service for manage phone alarm's and status bar bind's. */
        EternalService.start(context)
    }
}