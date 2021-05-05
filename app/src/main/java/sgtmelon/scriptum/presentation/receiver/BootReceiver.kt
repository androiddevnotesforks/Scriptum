package sgtmelon.scriptum.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Receiver which look for device boot.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        /**
         * Skip [EternalService.start] on low API.
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        if (context == null || intent == null) return

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            EternalService.start(context)
        }
    }
}