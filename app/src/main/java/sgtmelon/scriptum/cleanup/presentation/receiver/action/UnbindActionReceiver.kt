package sgtmelon.scriptum.cleanup.presentation.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindControl

/**
 * Receiver for handle click on unbind button in [BindControl].
 */
class UnbindActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

        if (id == Note.Default.ID) return

        GlobalScope.launch(Dispatchers.IO) {
            val broadcastControl = BroadcastControl[context]

            broadcastControl.sendCancelNoteBind(id)
            broadcastControl.sendUnbindNoteUI(id)
        }
    }

    companion object {
        operator fun get(context: Context, noteItem: NoteItem): PendingIntent {
            val intent = Intent(context, UnbindActionReceiver::class.java)
                .putExtra(Note.Intent.ID, noteItem.id)

            return PendingIntent.getBroadcast(
                context, noteItem.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}