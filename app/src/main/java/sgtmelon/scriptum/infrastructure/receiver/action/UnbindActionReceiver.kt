package sgtmelon.scriptum.infrastructure.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator

/**
 * Receiver for catch user click on "unbind" button inside statusBar notification.
 */
class UnbindActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Note.Key.ID, Note.Default.ID)
        if (id != Note.Default.ID) {
            val broadcast = BroadcastDelegator(context)
            broadcast.sendCancelNoteBind(id)
            broadcast.sendUnbindNoteUi(id)
        }
    }

    companion object {
        operator fun get(context: Context, item: NoteItem): PendingIntent {
            val intent = Intent(context, UnbindActionReceiver::class.java)
                .putExtra(Note.Key.ID, item.id)

            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            return PendingIntent.getBroadcast(context, item.id.toInt(), intent, flags)
        }
    }
}