package sgtmelon.scriptum.domain.interactor.callback.notification

import sgtmelon.scriptum.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.vm.callback.notification.IAlarmViewModel
import java.util.*

/**
 * Interface for communication [IAlarmViewModel] with [AlarmInteractor].
 */
interface IAlarmInteractor : IParentInteractor {

    @Repeat val repeat: Int

    val volume: Int

    val volumeIncrease: Boolean


    suspend fun getModel(id: Long): NoteItem?

    suspend fun setupRepeat(item: NoteItem, valueArray: IntArray, @Repeat repeat: Int): Calendar?

}