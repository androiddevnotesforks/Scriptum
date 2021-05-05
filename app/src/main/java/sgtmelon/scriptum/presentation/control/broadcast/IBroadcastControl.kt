package sgtmelon.scriptum.presentation.control.broadcast

import java.util.*

/**
 * Interface for [BroadcastControl]
 */
interface IBroadcastControl {

    fun sendUnbindNoteUI(id: Long)

    fun sendUpdateAlarmUI(id: Long)

    //region Bind functions

    fun sendNotifyNotesBind()

    fun sendCancelNoteBind(id: Long)

    fun sendNotifyInfoBind(count: Int?)

    //endregion

    //region Alarm functions

    fun sendTidyUpAlarm()

    fun sendSetAlarm(id: Long, calendar: Calendar, showToast: Boolean)

    fun sendCancelAlarm(id: Long)

    //endregion

}