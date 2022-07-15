package sgtmelon.scriptum.cleanup.presentation.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity


/**
 * Receiver for open [AlarmActivity] by time.
 */
class AlarmActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

        if (id == Note.Default.ID) return

        context.startActivity(SplashActivity.getAlarmInstance(context, id))
    }

    companion object {
        operator fun get(context: Context, id: Long): PendingIntent {
            val intent = Intent(context, AlarmActionReceiver::class.java)
                .putExtra(Note.Intent.ID, id)

            return PendingIntent.getBroadcast(
                context, id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}