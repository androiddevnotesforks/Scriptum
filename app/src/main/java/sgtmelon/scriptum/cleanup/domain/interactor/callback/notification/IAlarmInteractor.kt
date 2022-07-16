package sgtmelon.scriptum.cleanup.domain.interactor.callback.notification

import sgtmelon.scriptum.cleanup.domain.interactor.callback.IParentInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Repeat
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.notification.IAlarmViewModel
import java.util.*

/**
 * Interface for communication [IAlarmViewModel] with [AlarmInteractor].
 */
interface IAlarmInteractor : IParentInteractor {

    @Repeat val repeat: Int

    val volume: Int

    val isVolumeIncrease: Boolean


    suspend fun getModel(id: Long): NoteItem?

    suspend fun setupRepeat(item: NoteItem, valueArray: IntArray, @Repeat repeat: Int): Calendar?

}