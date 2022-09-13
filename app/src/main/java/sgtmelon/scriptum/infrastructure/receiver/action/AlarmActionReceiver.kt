package sgtmelon.scriptum.infrastructure.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note

/**
 * Receiver for open [AlarmActivity] by time.
 */
class AlarmActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)
        if (id != Note.Default.ID) {
            context.startActivity(SplashActivity.getAlarmInstance(context, id))
        }
    }

    companion object {
        operator fun get(context: Context, noteId: Long): PendingIntent {
            val intent = Intent(context, AlarmActionReceiver::class.java)
                .putExtra(Note.Intent.ID, noteId)

            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            return PendingIntent.getBroadcast(context, noteId.toInt(), intent, flags)
        }
    }
}