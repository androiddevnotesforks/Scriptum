package sgtmelon.scriptum.presentation.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Filter
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.repository.room.BindRepo

/**
 * Receiver for handle click on unbind button in [BindControl]
 */
class UnbindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, NoteData.Default.ID)

        if (id == NoteData.Default.ID) return

        GlobalScope.launch {
            if (BindRepo(context).unbindNote(id)) {
                BindControl[context].cancelNote(id.toInt())
            }

            context.apply {
                sendTo(Filter.MAIN, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
                sendTo(Filter.NOTE, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
            }
        }
    }

    companion object {
        operator fun get(context: Context, noteItem: NoteItem): PendingIntent {
            val intent = Intent(context, UnbindReceiver::class.java)
                    .putExtra(Values.NOTE_ID, noteItem.id)

            return PendingIntent.getBroadcast(
                    context, noteItem.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}