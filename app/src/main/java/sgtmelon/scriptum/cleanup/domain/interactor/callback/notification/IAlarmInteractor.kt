package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import java.util.Calendar
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import sgtmelon.scriptum.infrastructure.model.key.Repeat

/**
 * Interface for communication [IAlarmViewModel] with [AlarmInteractor].
 */
interface IAlarmInteractor : IParentInteractor {

    suspend fun getModel(id: Long): NoteItem?

    suspend fun setupRepeat(item: NoteItem, valueArray: IntArray, repeat: Repeat): Calendar?
}