package sgtmelon.scriptum.presentation.control.broadcast

import android.content.Context
import sgtmelon.common.getText
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
        context?.sendTo(Filter.MAIN, Command.UI.UNBIND_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }

        context?.sendTo(Filter.NOTE, Command.UI.UNBIND_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    override fun sendUpdateAlarmUI(id: Long) {
        context?.sendTo(Filter.MAIN, Command.UI.UPDATE_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    //region Bind functions

    override fun sendNotifyNotesBind() {
        context?.sendTo(Filter.SYSTEM, Command.System.NOTIFY_NOTES)
    }

    override fun sendCancelNoteBind(id: Long) {
        context?.sendTo(Filter.SYSTEM, Command.System.CANCEL_NOTE) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    override fun sendNotifyInfoBind(count: Int?) {
        context?.sendTo(Filter.SYSTEM, Command.System.NOTIFY_INFO)
    }

    override fun sendClearBind() {
        context?.sendTo(Filter.SYSTEM, Command.System.CLEAR_BIND)
    }

    //endregion

    //region Alarm functions

    override fun sendTidyUpAlarm() {
        context?.sendTo(Filter.SYSTEM, Command.System.TIDY_UP_ALARM)
    }

    override fun sendSetAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        context?.sendTo(Filter.SYSTEM, Command.System.SET_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
            putExtra(IntentData.Eternal.Intent.DATE, calendar.getText())
            putExtra(IntentData.Eternal.Intent.TOAST, showToast)
        }
    }

    override fun sendCancelAlarm(id: Long) {
        context?.sendTo(Filter.SYSTEM, Command.System.CANCEL_ALARM) {
            putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    override fun sendClearAlarm() {
        context?.sendTo(Filter.SYSTEM, Command.System.CLEAR_ALARM)
    }

    //endregion

    //region Eternal functions

    override fun sendEternalKill() {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.KILL)
    }

    override fun sendEternalPing() {
        context?.sendTo(Filter.ETERNAL, Command.Eternal.PING)
    }

    override fun sendEternalPong() {
        context?.sendTo(Filter.DEVELOP, Command.Eternal.PONG)
    }

    //endregion

    companion object {
        @RunPrivate var instance: IBroadcastControl? = null

        operator fun get(context: Context?): IBroadcastControl {
            return instance ?: BroadcastControl(context).also { instance = it }
        }
    }

}