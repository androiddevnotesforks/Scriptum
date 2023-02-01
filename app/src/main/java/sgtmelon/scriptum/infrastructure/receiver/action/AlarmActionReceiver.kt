package sgtmelon.scriptum.infrastructure.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity

/**
 * Receiver for open [AlarmActivity] by time.
 */
class AlarmActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val noteId = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)
        if (noteId != Note.Default.ID) {
            context.startActivity(InstanceFactory.Splash.getAlarm(context, noteId))
        }
    }

    companion object {
        operator fun get(context: Context, noteId: Long): PendingIntent {
            val intent = Intent(context, AlarmActionReceiver::class.java)
                .putExtra(Note.Intent.ID, noteId)

            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            return PendingIntent.getBroadcast(context, noteId.toInt(), intent, flags)
        }
    }
}