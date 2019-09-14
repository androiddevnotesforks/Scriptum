package sgtmelon.scriptum.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Filter
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.room.entity.NoteEntity

/**
 * Ресивер обработки нажатий по кнопкам для [BindControl]
 */
class UnbindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, Values.ND_NOTE_ID)

        if (id == Values.ND_NOTE_ID) return

        BindRepo(context).unbindNote(id)?.let {
            BindControl(context).setup(NoteModel(it)).cancelBind()
        }

        context.apply {
            sendTo(Filter.MAIN, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
            sendTo(Filter.NOTE, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
        }
    }

    companion object {
        operator fun get(context: Context, noteEntity: NoteEntity): PendingIntent {
            val intent = Intent(context, UnbindReceiver::class.java)
                    .putExtra(Values.NOTE_ID, noteEntity.id)

            return PendingIntent.getBroadcast(
                    context, noteEntity.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}