package sgtmelon.scriptum.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.control.notification.BindControl
import sgtmelon.scriptum.extension.sendTo
import sgtmelon.scriptum.model.data.ReceiverData.Command
import sgtmelon.scriptum.model.data.ReceiverData.Filter
import sgtmelon.scriptum.model.data.ReceiverData.Values
import sgtmelon.scriptum.repository.bind.BindRepo

/**
 * Ресивер обработки нажатий по кнопкам для [BindControl]
 *
 * @author SerjantArbuz
 */
class BindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(Values.NOTE_ID, Values.ND_NOTE_ID)

        if (id == Values.ND_NOTE_ID) return

        BindRepo.getInstance(context).unbindNote(id)?.let {
            BindControl(context, it).cancelBind()
        }

        context.apply {
            sendTo(Filter.MAIN, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
            sendTo(Filter.NOTE, Command.UNBIND_NOTE) { putExtra(Values.NOTE_ID, id) }
        }
    }

}