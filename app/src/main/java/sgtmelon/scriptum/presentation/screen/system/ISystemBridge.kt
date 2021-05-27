package sgtmelon.scriptum.presentation.screen.system

import androidx.annotation.MainThread
import sgtmelon.scriptum.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import java.util.*

/**
 * Interface for communication [ISystemInteractor] with [EternalService]
 */
interface ISystemBridge {

    //region Bind functions

    @MainThread fun notifyNotesBind(itemList: List<NoteItem>)

    @MainThread fun cancelNoteBind(id: Long)

    @MainThread fun notifyCountBind(count: Int)

    @MainThread fun clearBind()

    //endregion

    //region Alarm functions

    @MainThread fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean)

    @MainThread fun cancelAlarm(id: Long)

    @MainThread fun clearAlarm()

    //endregion

}