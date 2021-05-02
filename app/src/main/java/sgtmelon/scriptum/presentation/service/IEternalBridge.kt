package sgtmelon.scriptum.presentation.service

import androidx.annotation.MainThread
import sgtmelon.scriptum.domain.interactor.callback.eternal.IEternalInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import java.util.*

/**
 * Interface for communication [IEternalInteractor] with [EternalService]
 */
interface IEternalBridge {

    //region Alarm functions

    @MainThread fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean)

    @MainThread fun cancelAlarm(id: Long)

    //endregion

    //region Bind functions

    @MainThread fun notifyNotesBind(itemList: List<NoteItem>)

    @MainThread fun cancelNoteBind(id: Long)

    @MainThread fun notifyCountBind(count: Int)

    //endregion

}