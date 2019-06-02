package sgtmelon.scriptum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.model.key.ReceiverKey.Command
import sgtmelon.scriptum.model.key.ReceiverKey.Filter
import sgtmelon.scriptum.model.key.ReceiverKey.Values
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.repository.bind.BindRepo

/**
 * Ресивер обработки нажатий по кнопкам для [BindControl]
 *
 * @author SerjantArbuz
 */
class BindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, Values.ID_UNDEFINED)

        if (id == Values.ID_UNDEFINED) return

        val noteItem = BindRepo.getInstance(context).unbindNoteItem(id)
        BindControl(context, noteItem).cancelBind()

        context.apply {
            sendTo(Filter.MAIN, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
            sendTo(Filter.NOTE, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
        }
    }

}