package sgtmelon.scriptum.presentation.control.broadcast

import android.content.Context
import sgtmelon.extension.getText
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.data.ReceiverData.Command
import sgtmelon.scriptum.domain.model.data.ReceiverData.Filter
import sgtmelon.scriptum.extension.sendTo
import java.util.*

/**
 * Class for control broadcast messaging
 */
class BroadcastControl(private val context: Context?) : IBroadcastControl {

    /**
     * Function for let UI know about note unbind (throw status bar notification).
     */
    override fun sendUnbindNoteUI(id: Long) {
        context?.sendTo(Filter.MAIN, Command.UNBIND_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }

        context?.sendTo(Filter.NOTE, Command.UNBIND_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    override fun sendUpdateAlarmUI(id: Long) {
        context?.sendTo(Filter.MAIN, Command.UPDATE_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    //region Bind functions

    override fun sendNotifyNotesBind() {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.NOTIFY_NOTES)
    }

    override fun sendCancelNoteBind(id: Long) {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.CANCEL_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    override fun sendNotifyInfoBind(count: Int?) {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.NOTIFY_INFO)
    }

    //endregion

    //region Alarm functions

    override fun sendSetAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.SET_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
            putExtra(IntentData.Eternal.Intent.DATE, calendar.getText())
            putExtra(IntentData.Eternal.Intent.TOAST, showToast)
        }
    }

    override fun sendCancelAlarm(id: Long) {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.CANCEL_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    //endregion

    companion object {
        @RunPrivate var instance: IBroadcastControl? = null

        operator fun get(context: Context?): IBroadcastControl {
            return instance ?: BroadcastControl(context).also { instance = it }
        }
    }

}