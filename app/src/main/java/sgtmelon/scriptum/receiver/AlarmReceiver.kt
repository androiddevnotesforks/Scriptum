package sgtmelon.scriptum.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.model.item.NotificationItem
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity


/**
 * Receiver for open [AlarmActivity] by time
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, NoteData.Default.ID)

        if (id == NoteData.Default.ID) return

        context.startActivity(SplashActivity.getAlarmInstance(context, id))
    }

    companion object {
        operator fun get(context: Context, id: Long): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java)
                    .putExtra(Values.NOTE_ID, id)

            return PendingIntent.getBroadcast(
                    context, id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}