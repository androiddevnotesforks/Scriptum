package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Receiver which look for device boot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            EternalService.start(context)
        }
    }
}