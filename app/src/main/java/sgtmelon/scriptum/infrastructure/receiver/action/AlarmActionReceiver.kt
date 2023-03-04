package sgtmelon.scriptum.infrastructure.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity

/**
 * Receiver for open [AlarmActivity] by time.
 */
class AlarmActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val noteId = intent.getLongExtra(Note.Key.ID, Note.Default.ID)
        if (noteId != Note.Default.ID) {
            context.startActivity(SplashActivity.getAlarm(context, noteId))
        }
    }

    companion object {
        operator fun get(context: Context, noteId: Long): PendingIntent {
            val intent = context.intent<AlarmActionReceiver>(Note.Key.ID to noteId)

            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            return PendingIntent.getBroadcast(context, noteId.toInt(), intent, flags)
        }
    }
}