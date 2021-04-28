package sgtmelon.scriptum.presentation.receiver.action

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.data.provider.RoomProvider
import sgtmelon.scriptum.data.repository.room.BindRepo
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Filter
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.presentation.control.system.BindControl

/**
 * Receiver for handle click on unbind button in [BindControl].
 */
class UnbindActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Note.Intent.ID, Note.Default.ID)

        if (id == Note.Default.ID) return

        GlobalScope.launch(Dispatchers.IO) {
            // TODO send intent to EternalService and unbind note there.
            if (BindRepo(RoomProvider(context)).unbindNote(id)) {
                BindControl[null].cancelNote(id)
            }

            context.apply {
                sendTo(Filter.MAIN, Command.UNBIND_NOTE) { putExtra(Note.Intent.ID, id) }
                sendTo(Filter.NOTE, Command.UNBIND_NOTE) { putExtra(Note.Intent.ID, id) }
            }
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